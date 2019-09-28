package org.goblinframework.test.runner;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

abstract class StandaloneContainerCreator {

  @NotNull
  static Object createStandaloneContainer(@NotNull String... configLocations) {
    String className = "org.goblinframework.core.container.StandaloneSpringContainer";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      classLoader = StandaloneContainerCreator.class.getClassLoader();
    }
    try {
      Class<?> clazz = classLoader.loadClass(className);
      Constructor<?> constructor = clazz.getConstructor(String[].class);
      return constructor.newInstance(new Object[]{configLocations});
    } catch (Exception ex) {
      throw new UnsupportedOperationException(ex);
    }
  }
}
