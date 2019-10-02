package org.goblinframework.core.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

abstract public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {

  private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  @NotNull
  public static FastDateFormat getDefaultFormatter() {
    return FastDateFormat.getInstance(DEFAULT_PATTERN);
  }

  @NotNull
  public static String format(final long timestamp) {
    return Objects.requireNonNull(format(new Date(timestamp)));
  }

  @Nullable
  public static String format(@Nullable final Calendar calendar) {
    if (calendar == null) {
      return null;
    }
    return getDefaultFormatter().format(calendar);
  }

  @Nullable
  public static String format(@Nullable final Date date) {
    if (date == null) {
      return null;
    }
    return getDefaultFormatter().format(date);
  }

  @Nullable
  public static String format(@Nullable final Instant instant) {
    if (instant == null) {
      return null;
    }
    return getDefaultFormatter().format(Date.from(instant));
  }

  @Nullable
  public static Date parse(@Nullable final String s) {
    if (s == null) {
      return null;
    }
    String pattern = null;
    if (s.length() == 19
        && s.charAt(4) == '-'
        && s.charAt(7) == '-'
        && s.charAt(10) == ' '
        && s.charAt(13) == ':'
        && s.charAt(16) == ':') {
      char[] cs = s.toCharArray();
      for (int i = 0; i < cs.length; i++) {
        if (i == 4 || i == 7 || i == 10 || i == 13 || i == 16) {
          continue;
        }
        if (!Character.isDigit(cs[i])) {
          return null;
        }
      }
      pattern = "yyyy-MM-dd HH:mm:ss";
    } else if (s.length() == 23
        && s.charAt(4) == '-'
        && s.charAt(7) == '-'
        && s.charAt(10) == ' '
        && s.charAt(13) == ':'
        && s.charAt(16) == ':'
        && s.charAt(19) == '.') {
      char[] cs = s.toCharArray();
      for (int i = 0; i < cs.length; i++) {
        if (i == 4 || i == 7 || i == 10 || i == 13 || i == 16 || i == 19) {
          continue;
        }
        if (!Character.isDigit(cs[i])) {
          return null;
        }
      }
      pattern = DEFAULT_PATTERN;
    }
    if (pattern == null) {
      return null;
    }
    try {
      FastDateFormat formatter = FastDateFormat.getInstance(pattern);
      return formatter.parse(s);
    } catch (ParseException e) {
      return null;
    }
  }
}
