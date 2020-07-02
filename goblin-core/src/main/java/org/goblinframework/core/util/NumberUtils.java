package org.goblinframework.core.util;

import java.time.Instant;
import java.util.Date;

abstract public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

  public static int toInt(Object obj) {
    return toInt(obj, 0);
  }

  public static int toInt(Object obj, int defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    if (obj instanceof Integer) {
      return (Integer) obj;
    }
    if (obj instanceof Long) {
      // may lost precision
      return ((Long) obj).intValue();
    }
    String str = obj.toString();
    str = StringUtils.trim(str);
    return toInt(str, defaultValue);
  }

  public static long toLong(Object obj) {
    return toLong(obj, 0);
  }

  public static long toLong(Object obj, long defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    if (obj instanceof Long) {
      return (Long) obj;
    }
    if (obj instanceof Integer) {
      return ((Integer) obj).longValue();
    }
    if (obj instanceof Date) {
      return ((Date) obj).getTime();
    }
    if (obj instanceof Instant) {
      return ((Instant) obj).toEpochMilli();
    }
    String str = obj.toString();
    str = StringUtils.trim(str);
    return toLong(str, defaultValue);
  }

  public static double toDouble(Object obj) {
    return toDouble(obj, 0);
  }

  public static double toDouble(Object obj, double defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    if (obj instanceof Double) {
      return (Double) obj;
    }
    String str = obj.toString();
    str = StringUtils.trim(str);
    return toDouble(str, defaultValue);
  }

  public static float toFloat(Object obj) {
    return toFloat(obj, 0);
  }

  public static float toFloat(Object obj, float defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    if (obj instanceof Float) {
      return (Float) obj;
    }
    String str = obj.toString();
    str = StringUtils.trim(str);
    return toFloat(str, defaultValue);
  }
}
