package org.goblinframework.api.common;

import org.jetbrains.annotations.NotNull;

public interface GoblinFutureListener<T> {

  void futureCompleted(@NotNull GoblinFuture<T> future);

}
