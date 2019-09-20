package org.goblinframework.core.util;

import org.jetbrains.annotations.Nullable;

abstract public class PrimitiveUtils {

  public static boolean isPrimitiveWrapper(@Nullable Class<?> type) {
    return type == Boolean.class || type == Byte.class
        || type == Character.class || type == Double.class
        || type == Float.class || type == Integer.class
        || type == Long.class || type == Short.class;
  }
}
