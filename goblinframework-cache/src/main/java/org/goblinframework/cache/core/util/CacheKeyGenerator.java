package org.goblinframework.cache.core.util;

import org.goblinframework.cache.core.cache.CacheKeyPrefix;
import org.goblinframework.cache.core.cache.CacheKeyRevision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class CacheKeyGenerator {

  public static String generateCacheKey(Class<?> type, Object value) {
    return generateCacheKey(type, null, value);
  }

  public static String generateCacheKey(Class<?> type, String key, Object value) {
    return generateCacheKey(type, new String[]{key}, new Object[]{value});
  }

  public static String generateCacheKeyPrefix(@NotNull Class<?> type) {
    String prefix = type.getName();
    CacheKeyPrefix cacheKeyPrefix = type.getAnnotation(CacheKeyPrefix.class);
    if (cacheKeyPrefix != null) {
      String s = cacheKeyPrefix.prefix();
      if (s.trim().length() > 0) {
        prefix = s.trim();
      }
    }
    String revision = getCacheRevision(type);
    if (revision != null) {
      prefix = prefix + ":" + revision;
    }
    return prefix;
  }

  public static String generateCacheKey(Class<?> type, String[] keys, Object[] values) {
    String prefix = generateCacheKeyPrefix(type);
    return generateCacheKey(prefix, keys, values);
  }

  public static String generateCacheKey(Class<?> type, String[] keys, Object[] values, Object[] defaultValues) {
    String prefix = generateCacheKeyPrefix(type);
    return generateCacheKey(prefix, keys, values, defaultValues);
  }

  public static String generateCacheKey(String prefix, String[] keys, Object[] values) {
    return generateCacheKey(prefix, keys, values, null);
  }

  public static String generateCacheKey(String prefix, String[] keys, Object[] values, Object[] defaultValues) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    if (keys != null && keys.length != values.length) {
      throw new IllegalArgumentException();
    }
    if (defaultValues != null && defaultValues.length != values.length) {
      throw new IllegalArgumentException();
    }

    StringBuilder keyPart = new StringBuilder();
    for (int i = 0; i < values.length; i++) {
      if (keys != null && (keys[i] != null && keys[i].length() > 0)) {
        keyPart.append(keys[i]).append("=");
      }
      Object defaultValue = null;
      if (defaultValues != null) {
        defaultValue = defaultValues[i];
      }
      if (defaultValue == null) {
        keyPart.append(CacheKeyConverter.objectToCacheKey(values[i]));
      } else {
        String defVal = CacheKeyConverter.objectToCacheKey(defaultValue);
        keyPart.append(CacheKeyConverter.objectToCacheKey(values[i], defVal));
      }
      if (i != values.length - 1) {
        keyPart.append(",");
      }
    }
    return prefix + ":" + keyPart.toString();
  }

  @Nullable
  public static String getCacheRevision(@NotNull Class<?> type) {
    String revision = null;
    if (type.isAnnotationPresent(CacheKeyRevision.class)) {
      CacheKeyRevision annotation = type.getAnnotation(CacheKeyRevision.class);
      revision = annotation.revision().trim();
    }
    return revision;
  }
}
