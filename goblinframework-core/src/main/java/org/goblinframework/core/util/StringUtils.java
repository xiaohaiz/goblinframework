package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

  @NotNull
  public static String formalizeServers(@Nullable final String servers,
                                        @Nullable final String separator,
                                        @Nullable final Supplier<Integer> portSupplier) {
    if (isBlank(servers)) {
      return EMPTY;
    }
    String s = replace(servers, ",", " ");
    s = replace(s, ";", " ");
    List<String> formalized = Arrays.stream(split(s, " "))
        .filter(StringUtils::isNotBlank)
        .map(StringUtils::trim)
        .map(e -> {
          if (portSupplier == null) {
            return e;
          }
          Integer port = portSupplier.get();
          if (port == null) {
            return e;
          }
          if (contains(e, ":")) {
            return e;
          } else {
            return e + ":" + port;
          }
        })
        .distinct()
        .sorted()
        .collect(Collectors.toList());
    return join(formalized, (separator == null ? " " : separator));
  }
}
