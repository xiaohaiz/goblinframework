package org.goblinframework.cache.core.cache;

import org.goblinframework.api.cache.CacheValueModifier;
import org.goblinframework.api.cache.CasOperation;
import org.goblinframework.api.cache.GetResult;
import org.goblinframework.api.cache.GoblinCache;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CacheValueModifierImpl<V> implements CacheValueModifier<V> {
  private static final Logger logger = LoggerFactory.getLogger(CacheValueModifierImpl.class);

  private final GoblinCache cache;

  private String key;
  private Integer expiration;
  private CasOperation<V> modifier;

  CacheValueModifierImpl(@NotNull GoblinCache cache) {
    this.cache = cache;
  }

  @Override
  public CacheValueModifier<V> key(String key) {
    if (this.key != null) {
      throw new IllegalStateException("Key already set");
    }
    this.key = key;
    return this;
  }

  @Override
  public CacheValueModifier<V> expiration(int expiration) {
    if (this.expiration != null) {
      throw new IllegalStateException("Expiration already set");
    }
    this.expiration = expiration;
    return this;
  }

  @Override
  public CacheValueModifier<V> modifier(CasOperation<V> modifier) {
    if (this.modifier != null) {
      throw new IllegalStateException("Modifier already set");
    }
    this.modifier = modifier;
    return this;
  }

  @Override
  public void execute() {
    if (key == null) {
      throw new IllegalStateException("Key not set");
    }
    if (expiration == null) {
      throw new IllegalStateException("Expiration not set");
    }
    if (modifier == null) {
      throw new IllegalStateException("Modifier not set");
    }
    GetResult<V> cacheValue = cache.get(key);
    if (!cacheValue.hit) {
      logger.trace("[get] returns null, maybe cache server unavailable, evict the '{}'", key);
      cache.delete(key);
      return;
    }
    if (!cache.cas(key, expiration, cacheValue, modifier)) {
      logger.trace("[cas] failed, evict the '{}'", key);
      cache.delete(key);
    }
  }
}
