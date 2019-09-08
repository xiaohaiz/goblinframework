package org.goblinframework.core.util;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ReflectionUtils {

  public static boolean isToStringMethod(@Nullable Method method) {
    return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
  }
}
