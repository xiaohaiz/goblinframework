package org.goblinframework.core.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

final public class DateUtils {

  public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

  @NotNull
  public static String formatDate(@NotNull final Date date,
                                  @NotNull final String pattern) {
    return FastDateFormat.getInstance(pattern).format(date);
  }
}
