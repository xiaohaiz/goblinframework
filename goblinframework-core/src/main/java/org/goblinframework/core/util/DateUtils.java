package org.goblinframework.core.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

final public class DateUtils {

  @NotNull
  public static String formatDate(@NotNull final Date date) {
    return formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
  }

  @NotNull
  public static String formatDate(@NotNull final Date date,
                                  @NotNull final String pattern) {
    return FastDateFormat.getInstance(pattern).format(date);
  }
}
