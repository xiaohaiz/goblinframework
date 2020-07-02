package org.goblinframework.test.runner;

import java.lang.reflect.Method;

final class GoblinTestRunnerInitializer {

  static void initialize() {
    String className = "org.goblinframework.core.system.GoblinSystem";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      classLoader = GoblinTestRunnerInitializer.class.getClassLoader();
    }
    try {
      Class<?> clazz = classLoader.loadClass(className);
      Method uninstall = clazz.getMethod("uninstall");
      Thread shutdownHook = new Thread(() -> {
        try {
          uninstall.invoke(null);
        } catch (Exception ignore) {
        }
      });
      shutdownHook.setName("GoblinTestRunnerShutdownHook");
      shutdownHook.setDaemon(false);
      Runtime.getRuntime().addShutdownHook(shutdownHook);
      Method install = clazz.getMethod("install");
      install.invoke(null);
    } catch (Exception ex) {
      throw new UnsupportedOperationException(ex);
    }
  }
}
