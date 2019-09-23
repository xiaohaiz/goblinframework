package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;

public interface CacheLocationProvider {

  @NotNull
  CacheLocation getCacheSystemLocation();

  @NotNull
  default CacheSystem cacheSystem() {
    return getCacheSystemLocation().system;
  }

  @NotNull
  default String cacheName() {
    return getCacheSystemLocation().name;
  }
}
