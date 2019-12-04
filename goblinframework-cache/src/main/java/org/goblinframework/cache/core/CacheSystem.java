package org.goblinframework.cache.core;

import org.goblinframework.cache.core.cache.CacheBuilderManager2;
import org.jetbrains.annotations.Nullable;

public enum CacheSystem {

  NOP,
  JVM,
  CBS,
  RDS;

  @Nullable
  public CacheBuilder cacheBuilder() {
    return CacheBuilderManager2.INSTANCE.getCacheBuilder(this);
  }

  @Nullable
  public Cache cache(String name) {
    CacheBuilder builder = cacheBuilder();
    return builder == null ? null : builder.getCache(name);
  }
}
