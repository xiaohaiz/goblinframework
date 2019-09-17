package org.goblinframework.cache.core.spi;

import org.goblinframework.cache.core.cache.CacheSystem;
import org.goblinframework.cache.core.cache.GoblinCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder {

  @NotNull
  CacheSystem getCacheSystem();

  @Nullable
  GoblinCache getCache(@NotNull String name);

  void destroy();
}
