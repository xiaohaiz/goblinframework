package org.goblinframework.cache.core;

import org.jetbrains.annotations.NotNull;

public interface CacheLocationProvider {

  @NotNull
  CacheLocation getCacheSystemLocation();

  @NotNull
  default CacheSystem getCacheSystem() {
    return getCacheSystemLocation().system;
  }

  @NotNull
  default String getCacheName() {
    return getCacheSystemLocation().name;
  }
}
