package org.goblinframework.core.util;

import org.goblinframework.core.transcoder.ClassResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Class related miscellaneous utilities.
 */
abstract public class ClassUtils extends org.apache.commons.lang3.ClassUtils {

  private static final ClassResolver CLASS_RESOLVER;

  static {
    CLASS_RESOLVER = ServiceInstaller.installedFirst(ClassResolver.class);
  }

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
    if (CLASS_RESOLVER != null) {
      return CLASS_RESOLVER.resolve(className);
    }
    return getClass(getDefaultClassLoader(), className);
  }
}
