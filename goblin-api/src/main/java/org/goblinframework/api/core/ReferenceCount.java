package org.goblinframework.api.core;

public interface ReferenceCount {

  int count();

  void retain();

  void retain(int increment);

  boolean release();

  boolean release(int decrement);
}
