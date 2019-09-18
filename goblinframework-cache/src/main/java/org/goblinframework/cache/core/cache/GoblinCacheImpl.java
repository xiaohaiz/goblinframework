package org.goblinframework.cache.core.cache;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  @Nullable
  @Override
  public <T> T load(@Nullable String key) {
    GetResult<T> gr = get(key);
    return gr == null ? null : gr.value;
  }

  @Nullable
  @Override
  public <T> Boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation) {
    int maxTries = casOperation == null ? 0 : casOperation.getMaxTries();
    return cas(key, expirationInSeconds, getResult, maxTries, casOperation);
  }
}
