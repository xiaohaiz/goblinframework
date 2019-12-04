package org.goblinframework.cache.core.cache;

public interface CacheValueModifier<V> {

  CacheValueModifier<V> key(String key);

  CacheValueModifier<V> expiration(int expiration);

  CacheValueModifier<V> modifier(CasOperation<V> modifier);

  void execute();
}