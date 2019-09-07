package org.goblinframework.core.bootstrap;

import org.goblinframework.core.event.boss.EventBusBoss;
import org.goblinframework.core.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

final public class GoblinBootstrap {
  public static final Logger LOGGER = LoggerFactory.getLogger("goblin.core.Bootstrap");

  private static final AtomicBoolean initialized = new AtomicBoolean();
  private static final AtomicBoolean closed = new AtomicBoolean();

  public static void initialize() {
    if (initialized.compareAndSet(false, true)) {
      EventBusBoss.INSTANCE.initialize();
      GoblinModuleManager.INSTANCE.executeInitialize().executeBootstrap();
      LOGGER.info("WELCOME");
    }
  }

  public static void close() {
    if (closed.compareAndSet(false, true)) {
      GoblinModuleManager.INSTANCE.executeShutdown().executeFinalize();
      EventBusBoss.INSTANCE.close();
      LOGGER.info("FAREWELL");
      shutdownLog4j2IfNecessary();
    }
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
