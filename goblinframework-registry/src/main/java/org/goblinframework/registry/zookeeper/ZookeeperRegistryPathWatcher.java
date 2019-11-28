package org.goblinframework.registry.zookeeper;

import org.goblinframework.api.core.Lifecycle;
import org.goblinframework.api.function.Block1;
import org.goblinframework.api.function.Disposable;
import org.goblinframework.core.event.EventBus;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.SecondTimerEventListener;
import org.goblinframework.core.util.TimeAndUnit;
import org.goblinframework.registry.core.GoblinRegistryException;
import org.goblinframework.registry.core.RegistryChildListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

final public class ZookeeperRegistryPathWatcher implements Lifecycle, Disposable {

  private final ZookeeperRegistry registry;
  private String path;
  private TimeAndUnit reload;
  private Block1<List<String>> handler;

  private final AtomicReference<RegistryChildListener> listener = new AtomicReference<>();
  private final AtomicReference<SecondTimerEventListener> scheduler = new AtomicReference<>();
  private final AtomicReference<Semaphore> semaphore = new AtomicReference<>();
  private final AtomicBoolean termination = new AtomicBoolean();
  private final AtomicBoolean initialized = new AtomicBoolean();
  private final AtomicBoolean disposed = new AtomicBoolean();

  ZookeeperRegistryPathWatcher(@NotNull ZookeeperRegistry registry) {
    this.registry = registry;
  }

  @NotNull
  public ZookeeperRegistryPathWatcher path(@NotNull String path) {
    this.path = path;
    return this;
  }

  @NotNull
  public ZookeeperRegistryPathWatcher reload(long time, @NotNull TimeUnit unit) {
    this.reload = new TimeAndUnit(time, unit);
    return this;
  }

  @NotNull
  public ZookeeperRegistryPathWatcher handler(Block1<List<String>> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public void start() {
    if (path == null) {
      throw new GoblinRegistryException("Path is required");
    }
    if (handler == null) {
      throw new GoblinRegistryException("Handler is required");
    }
    if (!initialized.compareAndSet(false, true)) {
      return;
    }
    CountDownLatch latch = new CountDownLatch(1);
    RegistryChildListener listener = (parentPath, children) -> {
      latch.await();
      Semaphore s = semaphore.get();
      if (s != null) s.acquireUninterruptibly();
      try {
        if (termination.get()) {
          return;
        }
        handler.apply(children);
      } finally {
        if (s != null) s.release();
      }
    };
    registry.subscribeChildListener(path, listener);
    this.listener.set(listener);

    reload();
    latch.countDown();

    if (reload != null && reload.time > 0) {
      SecondTimerEventListener scheduler = new SecondTimerEventListener() {
        @Override
        protected long periodSeconds() {
          return reload.toSeconds();
        }

        @Override
        public void onEvent(@NotNull GoblinEventContext context) {
          reload();
        }
      };
      EventBus.subscribe(scheduler);
      this.scheduler.set(scheduler);
    }
  }

  @Override
  public boolean isRunning() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void stop() {
    dispose();
  }

  @Override
  public void dispose() {
    if (!disposed.compareAndSet(false, true)) {
      return;
    }
    semaphore.set(new Semaphore(1));
    semaphore.get().acquireUninterruptibly();
    try {
      termination.set(true);
      SecondTimerEventListener s = scheduler.getAndSet(null);
      if (s != null) {
        EventBus.unsubscribe(s);
      }
      RegistryChildListener l = listener.getAndSet(null);
      if (l != null) {
        registry.unsubscribeChildListener(path, l);
      }
    } finally {
      semaphore.get().release();
    }
  }

  private synchronized void reload() {
    List<String> children = registry.getChildren(path);
    handler.apply(children);
  }
}
