package org.goblinframework.core.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

abstract public class MapUtils {

  public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key, final Boolean defaultValue) {
    return org.apache.commons.collections4.MapUtils.getBoolean(map, key, defaultValue);
  }

  public static <K> String getString(final Map<? super K, ?> map, final K key) {
    return org.apache.commons.collections4.MapUtils.getString(map, key);
  }

  /**
   * Resort specified map according to keys and return LinkedHashMap.
   *
   * @param map  the source map to resort
   * @param keys the keys to provide sequence, null will be ignored
   * @param <K>  key type
   * @param <V>  value type
   * @return resorted LinkedHashMap
   */
  public static <K, V> Map<K, V> resort(Map<K, V> map, Collection<K> keys) {
    return resort(map, keys, null);
  }

  /**
   * Resort specified map according to keys and return LinkedHashMap. If the value
   * of found of specified key, use default value in case of it's not null.
   *
   * @param map          the source map to resort
   * @param keys         the keys to provide sequence, null will be ignored
   * @param defaultValue the default value
   * @param <K>          key type
   * @param <V>          value type
   * @return resorted LinkedHashMap
   */
  public static <K, V> Map<K, V> resort(Map<K, V> map, Collection<K> keys, V defaultValue) {
    Map<K, V> result = new LinkedHashMap<>();
    if (map == null) {
      return result;
    }
    if (keys == null) {
      result.putAll(map);
      return result;
    }
    keys.stream()
        .filter(Objects::nonNull)
        .forEach(k -> {
          V v = map.get(k);
          if (v != null) {
            result.putIfAbsent(k, v);
          } else {
            if (defaultValue != null) {
              result.putIfAbsent(k, defaultValue);
            }
          }
        });
    return result;
  }
}
