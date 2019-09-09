package org.goblinframework.core.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.Date;

abstract public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {

  @Nullable
  public static String format(@Nullable final Date date) {
    if (date == null) {
      return null;
    }
    return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS").format(date);
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
      pattern = "yyyy-MM-dd HH:mm:ss.SSS";
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
