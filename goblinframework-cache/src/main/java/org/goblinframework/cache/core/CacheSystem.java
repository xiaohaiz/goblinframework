package org.goblinframework.cache.core;

import org.goblinframework.cache.core.cache.CacheBuilderManager;
import org.jetbrains.annotations.Nullable;

public enum CacheSystem {

  NOP,
  JVM,
  CBS,
  RDS;

  @Nullable
  public CacheBuilder cacheBuilder() {
    return CacheBuilderManager.INSTANCE.cacheBuilder(this);
  }

  @Nullable
  public Cache cache(String name) {
    CacheBuilder builder = cacheBuilder();
    return builder == null ? null : builder.cache(name);
  }
}
