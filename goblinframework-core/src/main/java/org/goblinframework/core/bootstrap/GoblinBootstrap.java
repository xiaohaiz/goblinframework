package org.goblinframework.core.bootstrap;

import org.goblinframework.core.event.boss.EventBusBoss;

final public class GoblinBootstrap {

  public static void initialize() {
    EventBusBoss.INSTANCE.initialize();
    GoblinModuleManager.INSTANCE.executeInitialize().executeBootstrap();
  }

  public static void close() {
    GoblinModuleManager.INSTANCE.executeShutdown().executeFinalize();
    EventBusBoss.INSTANCE.close();
  }
}
