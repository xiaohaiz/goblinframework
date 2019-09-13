package org.goblinframework.core.util;

import java.util.Map;

abstract public class MapUtils {

  public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key, final Boolean defaultValue) {
    return org.apache.commons.collections4.MapUtils.getBoolean(map, key, defaultValue);
  }

  public static <K> String getString(final Map<? super K, ?> map, final K key) {
    return org.apache.commons.collections4.MapUtils.getString(map, key);
  }
}
