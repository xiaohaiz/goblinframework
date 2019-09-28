package org.goblinframework.core.system;

import org.goblinframework.api.system.IGoblinSystemManager;
import org.jetbrains.annotations.NotNull;

/**
 * GOBLIN system entrance.
 */
final public class GoblinSystem {

  public static void install() {
    IGoblinSystemManager.instance().start();
  }

  public static void uninstall() {
    IGoblinSystemManager.instance().stop();
  }

  @NotNull
  public static String applicationId() {
    return IGoblinSystemManager.instance().applicationId();
  }

  @NotNull
  public static String applicationName() {
    return IGoblinSystemManager.instance().applicationName();
  }

  @NotNull
  public static RuntimeMode runtimeMode() {
    return IGoblinSystemManager.instance().runtimeMode();
  }
}
