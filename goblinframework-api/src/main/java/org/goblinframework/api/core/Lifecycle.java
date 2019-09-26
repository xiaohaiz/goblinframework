package org.goblinframework.api.core;

public interface Lifecycle {

  void start();

  void stop();

  boolean isRunning();

}
