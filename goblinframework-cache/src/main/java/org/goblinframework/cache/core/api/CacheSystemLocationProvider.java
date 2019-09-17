package org.goblinframework.cache.core.api;

import org.jetbrains.annotations.NotNull;

public interface CacheSystemLocationProvider {

  @NotNull
  CacheSystemLocation getCacheSystemLocation();

  @NotNull
  default CacheSystem getCacheSystem() {
    return getCacheSystemLocation().getSystem();
  }

  @NotNull
  default String getName() {
    return getCacheSystemLocation().getName();
  }
}
