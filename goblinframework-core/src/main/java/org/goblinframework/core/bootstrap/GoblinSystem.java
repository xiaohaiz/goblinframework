package org.goblinframework.core.bootstrap;

import org.goblinframework.core.config.ConfigLoader;
import org.goblinframework.core.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

abstract public class GoblinSystem {

  private static final String applicationId;
  private static final String applicationName;

  static {
    applicationId = RandomUtils.nextObjectId();
    ConfigLoader configLoader = ConfigLoader.INSTANCE;
    applicationName = configLoader.getApplicationName();
  }

  /**
   * Trigger initialization.
   */
  public static void initialize() {
    GoblinBootstrap.LOGGER.info("Application name: {}", applicationName());
    GoblinBootstrap.LOGGER.info("Application id: {}", applicationId());
  }

  @NotNull
  public static String applicationId() {
    return applicationId;
  }

  @NotNull
  public static String applicationName() {
    return applicationName;
  }
}
