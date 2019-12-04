package org.goblinframework.cache.core.cache;

import org.goblinframework.cache.core.*;
import org.goblinframework.cache.core.annotation.GoblinCacheExpiration;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.core.util.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

final public class CacheValueLoaderImpl<K, V> implements CacheValueLoader<K, V> {
  private static final Logger logger = LoggerFactory.getLogger(CacheValueLoaderImpl.class);

  private final Cache cache;

  private KeyGenerator<K> keyGenerator;
  private ExternalLoader<K, V> externalLoader;
  private Integer expiration;
  private CacheHitListener cacheHitListener;
  private CacheMissListener cacheMissListener;
  private Collection<K> originalKeys;

  private final Map<K, String> keys = new LinkedHashMap<>();
  private final Map<K, V> values = new LinkedHashMap<>();
  private final Map<K, String> missedKeys = new LinkedHashMap<>();
  private final Map<K, V> missedValues = new LinkedHashMap<>();

  private boolean useValueWrapper = false;

  public CacheValueLoaderImpl(@NotNull Cache cache) {
    this.cache = cache;
  }

  @Override
  public CacheValueLoader<K, V> keyGenerator(KeyGenerator<K> keyGenerator) {
    if (this.keyGenerator != null) {
      throw new IllegalStateException();
    }
    this.keyGenerator = keyGenerator;
    return this;
  }

  @Override
  public CacheValueLoader<K, V> externalLoader(ExternalLoader<K, V> externalLoader) {
    if (this.externalLoader != null) {
      throw new IllegalStateException();
    }
    this.externalLoader = externalLoader;
    return this;
  }

  @Override
  public CacheValueLoader<K, V> expiration(int expiration) {
    if (this.expiration != null) {
      throw new IllegalStateException();
    }
    if (expiration < 0) {
      throw new IllegalArgumentException();
    }
    this.expiration = expiration;
    return this;
  }

  @Override
  public CacheValueLoader<K, V> keys(Collection<K> keys) {
    if (this.originalKeys != null) {
      throw new IllegalStateException();
    }
    this.originalKeys = keys;
    return this;
  }

  @Override
  public CacheValueLoader<K, V> cacheHitListener(CacheHitListener cacheHitListener) {
    this.cacheHitListener = cacheHitListener;
    return this;
  }

  @Override
  public CacheValueLoader<K, V> cacheMissListener(CacheMissListener cacheMissListener) {
    this.cacheMissListener = cacheMissListener;
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CacheValueLoader<K, V> loads() {
    if (keyGenerator == null) {
      throw new IllegalStateException();
    }
    Set<String> keyCollection = new LinkedHashSet<>();
    if (originalKeys != null) {
      originalKeys.stream()
          .filter(Objects::nonNull)
          .forEach(k -> {
            String key = keyGenerator.generate(k);
            if (key != null) {
              keyCollection.add(key);
              keys.put(k, key);
            }
          });
    }
    if (keyCollection.isEmpty()) {
      logger.trace("No cache key generated, nothing to be loaded from cache");
      return this;
    }
    Map<String, GetResult<Object>> responses = cache.gets(keyCollection);
    if (MapUtils.isEmpty(responses)) {
      missedKeys.putAll(keys);
      if (cacheMissListener != null) {
        missedKeys.values().forEach(cacheMissListener::failSafeOnCacheMiss);
      }
      return this;
    }
    keys.forEach((k, key) -> {
      GetResult<Object> response = responses.get(key);
      if (response == null || !response.hit) {
        missedKeys.put(k, key);
        if (cacheMissListener != null) {
          cacheMissListener.failSafeOnCacheMiss(key);
        }
      } else {
        if (cacheHitListener != null) {
          cacheHitListener.failSafeOnCacheHit(key);
        }
        Object val = response.value;
        if (val != null) {
          values.put(k, (V) val);
        }
      }
    });
    return this;
  }

  @Override
  public CacheValueLoader<K, V> loadsMissed() {
    if (!hasMissed()) {
      logger.trace("No missed keys, nothing to be loaded");
      return this;
    }
    if (externalLoader == null) {
      throw new IllegalStateException("No ExternalLoader set");
    }
    Map<K, V> missed = externalLoader.loadFromExternal(new LinkedHashSet<>(missedKeys.keySet()));
    if (MapUtils.isNotEmpty(missed)) {
      missedValues.putAll(missed);
    }
    return this;
  }

  @Override
  public CacheValueLoader<K, V> useValueWrapper(boolean value) {
    this.useValueWrapper = value;
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CacheValueLoader<K, V> writeAsList() {
    if (!hasMissed()) {
      return this;
    }
    Map<String, Object> keyValues = new LinkedHashMap<>();
    for (K source : missedKeys.keySet()) {
      String key = missedKeys.get(source);
      V value = missedValues.get(source);
      if (value == null) {
        value = (V) Collections.emptyList();
      }
      keyValues.put(key, value);
    }
    int expiration = NumberUtils.toInt(this.expiration, GoblinCacheExpiration.DEFAULT_EXPIRATION);
    keyValues.forEach((k, v) -> cache.add(k, expiration, v));
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CacheValueLoader<K, V> writeAsSet() {
    if (!hasMissed()) {
      return this;
    }
    Map<String, Object> keyValues = new LinkedHashMap<>();
    for (K source : missedKeys.keySet()) {
      String key = missedKeys.get(source);
      V value = missedValues.get(source);
      if (value == null) {
        value = (V) Collections.emptySet();
      }
      keyValues.put(key, value);
    }
    int expiration = NumberUtils.toInt(this.expiration, GoblinCacheExpiration.DEFAULT_EXPIRATION);
    keyValues.forEach((k, v) -> cache.add(k, expiration, v));
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CacheValueLoader<K, V> writeAsMap() {
    if (!hasMissed()) {
      return this;
    }
    Map<String, Object> keyValues = new LinkedHashMap<>();
    for (K source : missedKeys.keySet()) {
      String key = missedKeys.get(source);
      V value = missedValues.get(source);
      if (value == null) {
        value = (V) Collections.emptyMap();
      }
      keyValues.put(key, value);
    }
    int expiration = NumberUtils.toInt(this.expiration, GoblinCacheExpiration.DEFAULT_EXPIRATION);
    keyValues.forEach((k, v) -> cache.add(k, expiration, v));
    return this;
  }

  @Override
  public CacheValueLoader<K, V> writeAsWrapper() {
    if (!hasMissed()) {
      return this;
    }
    Map<String, Object> keyValues = new LinkedHashMap<>();
    for (K source : missedKeys.keySet()) {
      String key = missedKeys.get(source);
      V value = missedValues.get(source);
      if (value == null) {
        keyValues.put(key, new CacheValueWrapper(null));
      } else {
        keyValues.put(key, value);
      }
    }
    int expiration = NumberUtils.toInt(this.expiration, GoblinCacheExpiration.DEFAULT_EXPIRATION);
    keyValues.forEach((k, v) -> cache.add(k, expiration, v));
    return this;
  }

  @Override
  public CacheValueLoader<K, V> write() {
    if (useValueWrapper) {
      return writeAsWrapper();
    }
    if (!hasMissed()) {
      return this;
    }
    Map<String, Object> keyValues = new LinkedHashMap<>();
    for (K source : missedKeys.keySet()) {
      String key = missedKeys.get(source);
      V value = missedValues.get(source);
      if (value != null) {
        keyValues.put(key, value);
      }
    }
    int expiration = NumberUtils.toInt(this.expiration, GoblinCacheExpiration.DEFAULT_EXPIRATION);
    keyValues.forEach((k, v) -> cache.add(k, expiration, v));
    return this;
  }

  @Override
  public boolean hasMissed() {
    return !missedKeys.isEmpty();
  }

  @Override
  public Map<K, V> getResult() {
    Map<K, V> result = new LinkedHashMap<>();
    for (K source : keys.keySet()) {
      V value = values.get(source);
      if (value == null) {
        value = missedValues.get(source);
      }
      if (value != null) {
        result.put(source, value);
      }
    }
    return result;
  }

  @Override
  public Map<K, V> getAndResortResult() {
    return MapUtils.resort(getResult(), originalKeys);
  }

  @Override
  public Map<K, V> getAndResortResult(V defaultValue) {
    return MapUtils.resort(getResult(), originalKeys, defaultValue);
  }
}
