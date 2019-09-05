package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

final public class StringUtils {

  public static boolean isBlank(final CharSequence cs) {
    return org.apache.commons.lang3.StringUtils.isBlank(cs);
  }

  public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
    return org.apache.commons.lang3.StringUtils.defaultIfBlank(str, defaultStr);
  }

  public static <T extends CharSequence> T defaultIfBlank(final T str, @NotNull final Supplier<T> defaultStrSupplier) {
    return isBlank(str) ? defaultStrSupplier.get() : str;
  }
}
