package org.goblinframework.api.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface GoblinFuture<T> extends Future<T> {

  T getUninterruptibly();

  T getUninterruptibly(long timeout, @NotNull TimeUnit unit);

  GoblinFuture<T> awaitUninterruptibly();

  GoblinFuture<T> awaitUninterruptibly(long timeout, @NotNull TimeUnit unit);

  void addListener(@NotNull GoblinFutureListener<T> listener);

  void removeListener(@NotNull GoblinFutureListener<T> listener);

  GoblinFuture<T> complete(@Nullable T result);

  GoblinFuture<T> complete(@Nullable T result, @Nullable Throwable cause);

}
