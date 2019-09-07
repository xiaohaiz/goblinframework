package org.goblinframework.embedded.core;

import org.springframework.context.Lifecycle;

public interface EmbeddedServer extends Lifecycle {

  void start();

  void stop();

  boolean isRunning();

}
