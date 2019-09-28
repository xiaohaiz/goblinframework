package org.goblinframework.api.core;

import org.jetbrains.annotations.Nullable;

public class GoblinFutureResult<T> {

  @Nullable public T result;
  @Nullable public Throwable cause;

  GoblinFutureResult(@Nullable T result, @Nullable Throwable cause) {
    this.result = result;
    this.cause = cause;
  }
}
