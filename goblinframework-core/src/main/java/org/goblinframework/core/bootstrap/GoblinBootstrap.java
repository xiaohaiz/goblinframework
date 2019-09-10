package org.goblinframework.core.bootstrap;

import org.goblinframework.core.container.SpringContainerManager;
import org.goblinframework.core.event.boss.EventBusBoss;
import org.goblinframework.core.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

final public class GoblinBootstrap {
  public static final Logger LOGGER = LoggerFactory.getLogger("goblin.core.Bootstrap");

  public static void initialize() {
    EventBusBoss.INSTANCE.initialize();
    GoblinModuleManager.INSTANCE.executeInitialize().executeBootstrap();
    LOGGER.info("WELCOME");
  }

  public static void close() {
    SpringContainerManager.INSTANCE.close();
    GoblinModuleManager.INSTANCE.executeFinalize();
    EventBusBoss.INSTANCE.close();
    LOGGER.info("FAREWELL");
    shutdownLog4j2IfNecessary();
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
