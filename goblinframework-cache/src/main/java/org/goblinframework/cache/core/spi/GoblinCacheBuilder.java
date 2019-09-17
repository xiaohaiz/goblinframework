package org.goblinframework.cache.core.spi;

import org.goblinframework.cache.core.api.CacheSystem;
import org.goblinframework.cache.core.api.GoblinCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder {

  @NotNull
  CacheSystem getCacheSystem();

  @Nullable
  GoblinCache getCache(@NotNull String name);
}
