package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

abstract public class StringUtils extends org.apache.commons.lang3.StringUtils {

  @Nullable
  public static <T extends CharSequence> T defaultIfBlank(@Nullable final T str,
                                                          @NotNull final Supplier<T> defaultStrSupplier) {
    return isBlank(str) ? defaultStrSupplier.get() : str;
  }

  @Nullable
  public static String defaultString(@Nullable final String str,
                                     @NotNull final Supplier<String> defaultStrSupplier) {
    return str == null ? defaultStrSupplier.get() : str;
  }
}
