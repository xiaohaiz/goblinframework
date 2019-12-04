package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.Nullable;

public enum CacheSystem {

  NOP,
  JVM,
  CBS,
  RDS;

  @Nullable
  public CacheBuilder cacheBuilder() {
    return CacheBuilderManager.INSTANCE.getCacheBuilder(this);
  }

  @Nullable
  public Cache cache(String name) {
    CacheBuilder builder = cacheBuilder();
    return builder == null ? null : builder.getCache(name);
  }
}
