package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;

public interface CacheLocationProvider {

  @NotNull
  CacheLocation getCacheSystemLocation();

  @NotNull
  default CacheSystem getCacheSystem() {
    return getCacheSystemLocation().system;
  }

  @NotNull
  default String getName() {
    return getCacheSystemLocation().name;
  }
}
