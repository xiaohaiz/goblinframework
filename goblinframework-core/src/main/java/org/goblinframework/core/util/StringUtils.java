package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

final public class StringUtils {

  public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
    return org.apache.commons.lang3.StringUtils.contains(seq, searchSeq);
  }

  public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
    return org.apache.commons.lang3.StringUtils.defaultIfBlank(str, defaultStr);
  }

  public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
    return org.apache.commons.lang3.StringUtils.endsWith(str, suffix);
  }

  public static boolean isBlank(final CharSequence cs) {
    return org.apache.commons.lang3.StringUtils.isBlank(cs);
  }

  public static String replace(final String text, final String searchString, final String replacement) {
    return org.apache.commons.lang3.StringUtils.replace(text, searchString, replacement);
  }

  // ==========================================================================
  // EXTENSION UTILITIES METHODS
  // ==========================================================================

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
  public static <T extends CharSequence> T defaultIfBlank(final T str, @NotNull final Supplier<T> defaultStrSupplier) {
    return isBlank(str) ? defaultStrSupplier.get() : str;
  }
}
