package org.goblinframework.api.common;

public interface ReferenceCount {

  int count();

  void retain();

  void retain(int increment);

  boolean release();

  boolean release(int decrement);
}
