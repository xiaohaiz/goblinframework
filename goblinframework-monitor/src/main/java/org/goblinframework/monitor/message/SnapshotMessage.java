package org.goblinframework.monitor.message;

import java.util.concurrent.atomic.AtomicBoolean;

final public class SnapshotMessage implements TouchableMessage {

  private final AtomicBoolean touched = new AtomicBoolean();
  public Object payload;

  @Override
  public void touch() {
    touched.set(true);
  }

  @Override
  public boolean isTouched() {
    return touched.get();
  }
}
