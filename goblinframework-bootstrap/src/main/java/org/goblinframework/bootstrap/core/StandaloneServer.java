package org.goblinframework.bootstrap.core;

import org.goblinframework.core.bootstrap.GoblinBootstrap;
import org.goblinframework.core.util.ThreadUtils;

abstract public class StandaloneServer {

  public void bootstrap(String[] args) {
    GoblinBootstrap.initialize();
    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(new Thread("StandaloneServerShutdownHook") {
        @Override
        public void run() {
          shutdown();
        }
      });
    }

    doService();

    if (runDaemonMode()) {
      ThreadUtils.joinCurrentThread();
    }
  }

  public void shutdown() {
    GoblinBootstrap.close();
  }

  protected boolean useShutdownHook() {
    return true;
  }

  protected boolean runDaemonMode() {
    return true;
  }

  abstract protected void doService();
}
