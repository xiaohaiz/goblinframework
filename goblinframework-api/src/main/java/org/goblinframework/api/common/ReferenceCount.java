package org.goblinframework.api.common;

public interface ReferenceCount {

  int count();

  default void retain() {
    retain(1);
  }

  void retain(int increment);

  default boolean release() {
    return release(1);
  }

  boolean release(int decrement);
}
