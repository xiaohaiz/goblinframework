package org.goblinframework.cache.core;

import java.util.Collection;
import java.util.Map;

public interface CacheValueLoader<K, V> {

  CacheValueLoader<K, V> keyGenerator(KeyGenerator<K> keyGenerator);

  CacheValueLoader<K, V> externalLoader(ExternalLoader<K, V> externalLoader);

  CacheValueLoader<K, V> expiration(int expiration);

  CacheValueLoader<K, V> keys(Collection<K> keys);

  CacheValueLoader<K, V> cacheHitListener(CacheHitListener cacheHitListener);

  CacheValueLoader<K, V> cacheMissListener(CacheMissListener cacheMissListener);

  CacheValueLoader<K, V> loads();

  CacheValueLoader<K, V> loadsMissed();

  CacheValueLoader<K, V> useValueWrapper(boolean value);

  CacheValueLoader<K, V> writeAsList();

  CacheValueLoader<K, V> writeAsSet();

  CacheValueLoader<K, V> writeAsMap();

  CacheValueLoader<K, V> writeAsWrapper();

  CacheValueLoader<K, V> write();

  boolean hasMissed();

  Map<K, V> getResult();

  Map<K, V> getAndResortResult();

  Map<K, V> getAndResortResult(V defaultValue);
}
