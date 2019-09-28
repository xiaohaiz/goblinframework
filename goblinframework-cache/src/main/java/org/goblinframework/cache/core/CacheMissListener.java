package org.goblinframework.cache.core;

public interface CacheMissListener {

  default void failSafeOnCacheMiss(String key) {
    try {
      onCacheMiss(key);
    } catch (Exception ignored) {
    }
  }

  void onCacheMiss(String key);
}
