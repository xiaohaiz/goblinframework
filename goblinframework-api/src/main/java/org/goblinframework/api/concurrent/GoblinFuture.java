package org.goblinframework.api.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface GoblinFuture<T> extends Future<T> {

  T getUninterruptibly();

  T getUninterruptibly(long timeout, TimeUnit unit);

  GoblinFuture<T> awaitUninterruptibly();

  GoblinFuture<T> awaitUninterruptibly(long timeout, TimeUnit unit);

  void addListener(GoblinFutureListener<T> listener);

  void removeListener(GoblinFutureListener<T> listener);

  GoblinFuture<T> complete(T result);

  GoblinFuture<T> complete(T result, Throwable cause);

}
