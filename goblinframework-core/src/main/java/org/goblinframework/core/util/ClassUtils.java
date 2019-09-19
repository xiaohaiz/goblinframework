package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class related miscellaneous utilities.
 */
abstract public class ClassUtils extends org.apache.commons.lang3.ClassUtils {

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
    return getClass(getDefaultClassLoader(), className);
  }

  public static List<Class<?>> getClassInheritanceHierarchy(@NotNull final Class<?> clazz, boolean topEndian) {
    Class<?> theClass = clazz;
    List<Class<?>> chain = new LinkedList<>();
    chain.add(theClass);
    while ((theClass = theClass.getSuperclass()) != null) {
      chain.add(theClass);
    }
    if (topEndian) {
      Collections.reverse(chain);
    }
    return Collections.unmodifiableList(chain);
  }
}
