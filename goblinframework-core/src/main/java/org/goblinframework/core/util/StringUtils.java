package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

final public class StringUtils extends org.apache.commons.lang3.StringUtils {

  @Nullable
  public static String compactContinuousSlashes(@Nullable final String path) {
    if (path == null) {
      return null;
    }
    String s = path;
    if (contains(s, "//")) {
      while (contains(s, "//")) {
        s = replace(s, "//", "/");
      }
    }
    if ("/".equals(s)) {
      return "/";
    }
    if (endsWith(s, "/")) {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }

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
