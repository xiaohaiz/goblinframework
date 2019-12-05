package org.goblinframework.registry.zookeeper;

import org.goblinframework.core.event.EventBus;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.timer.SecondTimerEventListener;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.TimeAndUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@GoblinManagedBean(type = "Registry")
final public class ZookeeperRegistryPathKeeper extends GoblinManagedObject
    implements ZookeeperRegistryPathKeeperMXBean {

  @NotNull private final ZookeeperRegistry registry;
  private final AtomicReference<TimeAndUnit> period = new AtomicReference<>();
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Map<String, PathData> buffer = new HashMap<>();
  private final AtomicReference<SecondTimerEventListener> scheduler = new AtomicReference<>();
  private final Semaphore semaphore = new Semaphore(1);

  ZookeeperRegistryPathKeeper(@NotNull ZookeeperRegistry registry) {
    this.registry = registry;
  }

  public ZookeeperRegistryPathKeeper scheduler(long time, @NotNull TimeUnit unit) {
    period.set(new TimeAndUnit(time, unit));
    return this;
  }

  public void watch(@NotNull String path, boolean ephemeral, @Nullable Object data) {
    lock.writeLock().lock();
    try {
      if (!buffer.containsKey(path)) {
        PathData pathData = new PathData(path, ephemeral, data);
        buffer.put(path, pathData);
        if (!registry.exists(path)) {
          initializePathData(pathData);
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void unwatch(@NotNull String path) {
    lock.writeLock().lock();
    PathData removed;
    try {
      removed = buffer.remove(path);
    } finally {
      lock.writeLock().unlock();
    }
    if (removed != null) {
      try {
        registry.delete(path);
        logger.debug("Deleted: {}", path);
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  protected void initializeBean() {
    TimeAndUnit period = this.period.get();
    if (period != null && period.time > 0) {
      SecondTimerEventListener scheduler = new SecondTimerEventListener() {
        @Override
        protected long periodSeconds() {
          return period.toSeconds();
        }

        @Override
        public void onEvent(@NotNull GoblinEventContext context) {
          semaphore.acquireUninterruptibly();
          try {
            lock.readLock().lock();
            try {
              for (PathData pathData : buffer.values()) {
                if (!registry.exists(pathData.path)) {
                  initializePathData(pathData);
                }
              }
            } finally {
              lock.readLock().unlock();
            }
          } finally {
            semaphore.release();
          }
        }
      };
      EventBus.subscribe(scheduler);
      this.scheduler.set(scheduler);
    }
  }

  @Override
  protected void disposeBean() {
    SecondTimerEventListener scheduler = this.scheduler.getAndSet(null);
    if (scheduler != null) {
      EventBus.unsubscribe(scheduler);
    }
    semaphore.acquireUninterruptibly();
    try {
      lock.writeLock().lock();
      try {
        buffer.values().forEach(e -> {
          String path = e.path;
          try {
            registry.delete(path);
            logger.debug("Deleted: {}", path);
          } catch (Exception ignore) {
          }
        });
        buffer.clear();
      } finally {
        lock.writeLock().unlock();
      }
    } finally {
      semaphore.release();
    }
  }

  private void initializePathData(PathData pathData) {
    if (pathData.ephemeral) {
      if (pathData.data == null) {
        registry.createEphemeral(pathData.path);
        logger.debug("Created(E): {}", pathData.path);
      } else {
        registry.createEphemeral(pathData.path, pathData.data);
        logger.debug("Created(E): {}", pathData.path);
      }
    } else {
      if (pathData.data == null) {
        registry.createPersistent(pathData.path);
        logger.debug("Created(P): {}", pathData.path);
      } else {
        registry.createPersistent(pathData.path, pathData.data);
        logger.debug("Created(P): {}", pathData.path);
      }
    }
  }

  private static class PathData {
    private String path;
    private boolean ephemeral;
    private Object data;

    private PathData(String path, boolean ephemeral, Object data) {
      this.path = path;
      this.ephemeral = ephemeral;
      this.data = data;
    }
  }
}
