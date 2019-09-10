package org.goblinframework.api.common;

public interface Lifecycle {

  void start();

  void stop();

  boolean isRunning();

}
