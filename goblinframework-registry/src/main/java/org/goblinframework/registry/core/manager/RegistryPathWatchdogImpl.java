package org.goblinframework.registry.core.manager;

import org.goblinframework.api.common.TimeAndUnit;
import org.goblinframework.api.event.EventBus;
import org.goblinframework.api.event.GoblinEventContext;
import org.goblinframework.api.registry.Registry;
import org.goblinframework.api.registry.RegistryPathWatchdog;
import org.goblinframework.core.event.SecondTimerEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final public class RegistryPathWatchdogImpl implements RegistryPathWatchdog {

  @NotNull private final Registry registry;
  @NotNull private final TimeAndUnit period;

  private final AtomicBoolean initialized = new AtomicBoolean();
  private final AtomicBoolean disposed = new AtomicBoolean();
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Map<String, PathData> buffer = new HashMap<>();
  private final AtomicReference<SecondTimerEventListener> scheduler = new AtomicReference<>();
  private final Semaphore semaphore = new Semaphore(1);

  RegistryPathWatchdogImpl(@NotNull Registry registry, @NotNull TimeAndUnit period) {
    this.registry = registry;
    this.period = period;
  }

  @Override
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

  @Override
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
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  public void initialize() {
    if (!initialized.compareAndSet(false, true)) {
      return;
    }
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

  @Override
  public void dispose() {
    if (!disposed.compareAndSet(false, true)) {
      return;
    }
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
      } else {
        registry.createEphemeral(pathData.path, pathData.data);
      }
    } else {
      if (pathData.data == null) {
        registry.createPersistent(pathData.path);
      } else {
        registry.createPersistent(pathData.path, pathData.data);
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
