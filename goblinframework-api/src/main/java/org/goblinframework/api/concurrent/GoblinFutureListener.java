package org.goblinframework.api.concurrent;

public interface GoblinFutureListener<T> {

  void futureCompleted(GoblinFuture<T> future);

}
