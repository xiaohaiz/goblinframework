package org.goblinframework.core.bootstrap;

import org.goblinframework.core.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

abstract public class GoblinSystem {

  private static final String applicationId;

  static {
    applicationId = RandomUtils.nextObjectId();
  }

  /**
   * Trigger initialization.
   */
  public static void initialize() {
  }

  @NotNull
  public static String applicationId() {
    return applicationId;
  }
}
