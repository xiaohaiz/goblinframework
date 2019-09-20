package org.goblinframework.cache.core.cache;

import org.goblinframework.cache.core.annotation.CacheSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder extends GoblinCacheNameDecorator {

  @NotNull
  CacheSystem getCacheSystem();

  @Nullable
  GoblinCache getCache(@NotNull String name);

}
