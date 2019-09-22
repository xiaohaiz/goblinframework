package org.goblinframework.cache.core.cache;

import org.goblinframework.core.cache.GoblinCacheSystem;
import org.goblinframework.core.cache.GoblinCacheSystemLocation;
import org.jetbrains.annotations.NotNull;

public interface CacheSystemLocationProvider {

  @NotNull
  GoblinCacheSystemLocation getCacheSystemLocation();

  @NotNull
  default GoblinCacheSystem getCacheSystem() {
    return getCacheSystemLocation().getSystem();
  }

  @NotNull
  default String getName() {
    return getCacheSystemLocation().getName();
  }
}
