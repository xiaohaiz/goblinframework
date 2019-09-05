package org.goblinframework.core.concurrent;

import org.goblinframework.api.concurrent.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class GoblinFutureImpl<T> implements GoblinFuture<T> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicInteger order = new AtomicInteger();
  private final CountDownLatch latch = new CountDownLatch(1);
  private final AtomicReference<GoblinFutureResult<T>> result = new AtomicReference<>();
  private final ReentrantLock lock = new ReentrantLock();
  private final IdentityHashMap<GoblinFutureListener<T>, GoblinFutureListenerDelegator<T>> listeners = new IdentityHashMap<>();
  private final AtomicBoolean completed = new AtomicBoolean();

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return latch.getCount() == 0;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    latch.await();
    GoblinFutureResult<T> fr = result.get();
    assert fr != null;
    if (fr.getRight() != null) {
      throw new ExecutionException(fr.getRight());
    }
    return fr.getLeft();
  }

  @Override
  public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    boolean success = latch.await(timeout, unit);
    if (!success) {
      throw new TimeoutException();
    }
    GoblinFutureResult<T> fr = result.get();
    assert fr != null;
    if (fr.getRight() != null) {
      throw new ExecutionException(fr.getRight());
    }
    return fr.getLeft();
  }

  @Override
  public T getUninterruptibly() {
    try {
      return get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      throw new GoblinExecutionException(ex);
    }
  }

  @Override
  public T getUninterruptibly(long timeout, @NotNull TimeUnit unit) {
    try {
      return get(timeout, unit);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      throw new GoblinExecutionException(ex);
    } catch (TimeoutException ex) {
      throw new GoblinTimeoutException(ex);
    }
  }

  @Override
  public GoblinFuture<T> awaitUninterruptibly() {
    getUninterruptibly();
    return this;
  }

  @Override
  public GoblinFuture<T> awaitUninterruptibly(long timeout, @NotNull TimeUnit unit) {
    getUninterruptibly(timeout, unit);
    return this;
  }

  @Override
  public void addListener(@NotNull GoblinFutureListener<T> listener) {
    lock.lock();
    try {
      if (listeners.containsKey(listener)) {
        return;
      }
      GoblinFutureListenerDelegator<T> delegator = new GoblinFutureListenerDelegator<>(listener, order.getAndIncrement());
      listeners.put(listener, delegator);
    } finally {
      lock.unlock();
    }
    if (isDone()) {
      executeListenersAfterCompleted();
    }
  }

  @Override
  public void removeListener(@NotNull GoblinFutureListener<T> listener) {
    lock.lock();
    try {
      listeners.remove(listener);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public GoblinFuture<T> complete(T result) {
    return complete(result, null);
  }

  @Override
  public GoblinFuture<T> complete(T result, Throwable cause) {
    if (!completed.compareAndSet(false, true)) {
      return this;
    }
    this.result.set(new GoblinFutureResult<>(result, cause));
    this.latch.countDown();
    this.executeListenersAfterCompleted();
    return this;
  }

  private void executeListenersAfterCompleted() {
    List<GoblinFutureListenerDelegator<T>> listenerList;
    lock.lock();
    try {
      if (listeners.isEmpty()) {
        return;
      }
      listenerList = new LinkedList<>(listeners.values());
      listeners.clear();
    } finally {
      lock.unlock();
    }
    for (GoblinFutureListenerDelegator<T> listener : listenerList) {
      try {
        listener.futureCompleted(this);
      } catch (Exception ex) {
        logger.warn("Exception caught when executing GoblinFutureListener: {}", listener.getDelegator(), ex);
      }
    }
  }
}
