package org.goblinframework.api.concurrent;

public interface GoblinFutureListener<T> {

  void futureCompleted(T result, Throwable cause);

}
