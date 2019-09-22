package org.goblinframework.cache.core.cache;

import org.goblinframework.core.cache.GoblinCacheSystem;
import org.jetbrains.annotations.NotNull;

public interface CacheSystemLocationProvider {

  @NotNull
  CacheSystemLocation getCacheSystemLocation();

  @NotNull
  default GoblinCacheSystem getCacheSystem() {
    return getCacheSystemLocation().getSystem();
  }

  @NotNull
  default String getName() {
    return getCacheSystemLocation().getName();
  }
}
