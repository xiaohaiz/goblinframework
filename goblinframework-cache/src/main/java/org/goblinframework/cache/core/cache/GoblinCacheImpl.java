package org.goblinframework.cache.core.cache;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;

@GoblinManagedBean(type = "CACHE")
abstract public class GoblinCacheImpl extends GoblinManagedObject implements GoblinCache {

  private final CacheSystemLocation location;

  protected GoblinCacheImpl(@NotNull CacheSystemLocation location) {
    this.location = location;
  }

  @NotNull
  @Override
  public CacheSystemLocation getCacheSystemLocation() {
    return location;
  }
}
