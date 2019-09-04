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
}
