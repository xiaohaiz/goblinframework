package org.goblinframework.api.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface GoblinFuture<T> extends Future<T> {

  T getUninterruptibly();

  T getUninterruptibly(long timeout, @NotNull TimeUnit unit);

  GoblinFuture<T> awaitUninterruptibly();

  GoblinFuture<T> awaitUninterruptibly(long timeout, @NotNull TimeUnit unit);

  void addListener(@NotNull GoblinFutureListener<T> listener);

  void removeListener(@NotNull GoblinFutureListener<T> listener);

  GoblinFuture<T> complete(T result);

  GoblinFuture<T> complete(T result, Throwable cause);

}
