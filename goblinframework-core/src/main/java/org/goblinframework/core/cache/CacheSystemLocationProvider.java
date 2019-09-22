package org.goblinframework.core.cache;

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
