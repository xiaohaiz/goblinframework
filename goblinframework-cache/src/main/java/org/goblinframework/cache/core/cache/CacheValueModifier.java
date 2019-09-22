package org.goblinframework.cache.core.cache;

import org.goblinframework.core.cache.CasOperation;

public interface CacheValueModifier<V> {

  CacheValueModifier<V> key(String key);

  CacheValueModifier<V> expiration(int expiration);

  CacheValueModifier<V> modifier(CasOperation<V> modifier);

  void execute();
}