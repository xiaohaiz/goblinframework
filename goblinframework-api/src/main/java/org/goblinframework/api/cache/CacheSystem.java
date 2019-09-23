package org.goblinframework.api.cache;

import org.jetbrains.annotations.Nullable;

public enum CacheSystem {

  NOP,
  JVM,
  CBS,
  RDS;

  @Nullable
  public CacheBuilder cacheBuilder() {
    ICacheBuilderManager manager = ICacheBuilderManager.instance();
    return manager == null ? null : manager.cacheBuilder(this);
  }

  @Nullable
  public Cache cache(String name) {
    CacheBuilder builder = cacheBuilder();
    return builder == null ? null : builder.cache(name);
  }

  @Nullable
  public Cache defaultCache() {
    return cache(this.name());
  }
}
