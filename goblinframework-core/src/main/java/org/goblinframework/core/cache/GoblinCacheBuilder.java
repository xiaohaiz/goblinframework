package org.goblinframework.core.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder extends GoblinCacheNameDecorator {

  @NotNull
  GoblinCacheSystem getCacheSystem();

  @Nullable
  GoblinCache getCache(@NotNull String name);

}
