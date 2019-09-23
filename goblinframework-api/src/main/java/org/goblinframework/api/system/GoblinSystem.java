package org.goblinframework.api.system;

import org.jetbrains.annotations.NotNull;

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
}
