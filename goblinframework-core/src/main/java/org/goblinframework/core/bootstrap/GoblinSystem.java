package org.goblinframework.core.bootstrap;

import org.goblinframework.core.config.ConfigLoader;
import org.goblinframework.core.container.SpringContainerManager;
import org.goblinframework.core.event.boss.EventBusBoss;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

abstract public class GoblinSystem {
  public static final Logger LOGGER = LoggerFactory.getLogger("goblin.system");

  private static final String applicationId = RandomUtils.nextObjectId();
  private static final AtomicBoolean installed = new AtomicBoolean();
  private static final AtomicBoolean uninstalled = new AtomicBoolean();

  public static void install() {
    if (!installed.compareAndSet(false, true)) {
      return;
    }
    ConfigLoader.INSTANCE.initialize();
    EventBusBoss.INSTANCE.initialize();
    ConfigLoader.INSTANCE.start();
    GoblinModuleManager.INSTANCE.executeInitialize().executeBootstrap();
    LOGGER.info("WELCOME");
  }

  public static void uninstall() {
    if (!uninstalled.compareAndSet(false, true)) {
      return;
    }
    SpringContainerManager.INSTANCE.dispose();
    GoblinModuleManager.INSTANCE.executeFinalize();
    ConfigLoader.INSTANCE.stop();
    EventBusBoss.INSTANCE.dispose();
    ConfigLoader.INSTANCE.dispose();
    LOGGER.info("FAREWELL");
    shutdownLog4j2IfNecessary();
  }

  @NotNull
  public static String getApplicationId() {
    return applicationId;
  }

  @NotNull
  public static String getApplicationName() {
    return ConfigLoader.INSTANCE.getApplicationName();
  }

  private static void shutdownLog4j2IfNecessary() {
    try {
      Class<?> clazz = ClassUtils.loadClass("org.apache.logging.log4j.LogManager");
      Method method = clazz.getMethod("shutdown");
      method.invoke(null);
    } catch (Exception ignore) {
    }
  }
}
