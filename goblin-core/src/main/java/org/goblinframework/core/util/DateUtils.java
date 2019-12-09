package org.goblinframework.core.util;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

abstract public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

  public static int getCurrentToDayEndSecond() {
    // 获取从当前时间到当天截止的秒数，主要用于缓存的过期时间
    Instant instant = Instant.now();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(instant.toEpochMilli());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.add(Calendar.MILLISECOND, 86400 * 1000);
    calendar.add(Calendar.MILLISECOND, -1);
    // 保证至少留1秒，免得缓存不过期
    return Math.max(1, (int) instant.until(calendar.toInstant(), ChronoUnit.SECONDS));
  }

  public static int getCurrentToWeekEndSecond() {
    // 获取从当前时间到本周截止的秒数，主要用于缓存的过期时间
    Instant instant = Instant.now();
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.setTimeInMillis(instant.toEpochMilli());
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.add(Calendar.MILLISECOND, 86400 * 7 * 1000);
    calendar.add(Calendar.MILLISECOND, -1);
    // 保证至少留1秒，免得缓存不过期
    return Math.max(1, (int) instant.until(calendar.toInstant(), ChronoUnit.SECONDS));
  }

  public static int getCurrentToMonthEndSecond() {
    // 获取从当前时间到本月截止的秒数，主要用于缓存的过期时间
    // 这个时间也许会超过30天，如果是需要再换算一下到unix时间
    Instant instant = Instant.now();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(instant.toEpochMilli());
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.add(Calendar.MONTH, 1);
    calendar.add(Calendar.MILLISECOND, -1);
    // 保证至少留1秒，免得缓存不过期
    return Math.max(1, (int) instant.until(calendar.toInstant(), ChronoUnit.SECONDS));
  }

  @Nullable
  public static Date parseDate(@Nullable Object obj) {
    if (obj instanceof Date) {
      return (Date) obj;
    }
    if (obj instanceof Calendar) {
      return ((Calendar) obj).getTime();
    }
    if (obj instanceof Instant) {
      return Date.from((Instant) obj);
    }
    if (obj instanceof Integer || obj instanceof Long || obj instanceof Float || obj instanceof Double) {
      Number number = (Number) obj;
      return new Date(number.longValue());
    }
    if (obj instanceof String) {
      return DateFormatUtils.parse((String) obj);
    }
    return null;
  }
}
