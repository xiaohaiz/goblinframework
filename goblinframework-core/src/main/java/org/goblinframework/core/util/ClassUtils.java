package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;

/**
 * Class related miscellaneous utilities.
 */
final public class ClassUtils {

  @NotNull
  public static ClassLoader getDefaultClassLoader() {
    ClassLoader classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
    if (classLoader == null) {
      throw new UnsupportedOperationException("Cannot access ClassLoader");
    }
    return classLoader;
  }

  @NotNull
  public static Class<?> filterCglibProxyClass(@NotNull Class<?> clazz) {
    Class<?> clazzForUse = clazz;
    while (org.springframework.util.ClassUtils.isCglibProxyClass(clazzForUse)) {
      clazzForUse = clazzForUse.getSuperclass();
    }
    return clazzForUse;
  }

  @NotNull
  public static Class<?> loadClass(@NotNull String className) throws ClassNotFoundException {
    return org.apache.commons.lang3.ClassUtils.getClass(getDefaultClassLoader(), className);
  }
}
