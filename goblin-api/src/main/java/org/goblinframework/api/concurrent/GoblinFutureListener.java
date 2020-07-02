package org.goblinframework.api.concurrent;

import org.jetbrains.annotations.NotNull;

public interface GoblinFutureListener<T> {

  void futureCompleted(@NotNull GoblinFuture<T> future);

}
