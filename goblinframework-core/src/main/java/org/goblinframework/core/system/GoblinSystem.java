package org.goblinframework.core.system;

import org.jetbrains.annotations.NotNull;

/**
 * GOBLIN system entrance.
 */
final public class GoblinSystem {

  public static void install() {
    GoblinSystemManager.INSTANCE.start();
  }

  public static void uninstall() {
    GoblinSystemManager.INSTANCE.stop();
  }

  @NotNull
  public static String applicationId() {
    return GoblinSystemManager.INSTANCE.applicationId();
  }

  @NotNull
  public static String applicationName() {
    return GoblinSystemManager.INSTANCE.applicationName();
  }

  @NotNull
  public static RuntimeMode runtimeMode() {
    return GoblinSystemManager.INSTANCE.runtimeMode();
  }
}
