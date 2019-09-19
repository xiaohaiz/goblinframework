package org.goblinframework.core.conversion;

import org.goblinframework.core.util.StringUtils;

abstract public class ConversionUtils {

  public static boolean toBoolean(Object obj) {
    return toBoolean(obj, false);
  }

  public static boolean toBoolean(Object obj, boolean defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    if (obj instanceof Boolean) {
      return (Boolean) obj;
    }
    String str = obj.toString();
    str = StringUtils.trim(str);
    return StringUtils.equals(str, "true");
  }
}
