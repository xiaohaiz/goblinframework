package org.goblinframework.cache.core.cache;

import org.goblinframework.core.cache.GoblinCache;
import org.goblinframework.core.cache.GoblinCacheNameDecorator;
import org.goblinframework.core.cache.GoblinCacheSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder extends GoblinCacheNameDecorator {

  @NotNull
  GoblinCacheSystem getCacheSystem();

  @Nullable
  GoblinCache getCache(@NotNull String name);

}
