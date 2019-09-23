package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCacheBuilder extends CacheNameDecorator {

  @Nullable
  GoblinCache getCache(@NotNull String name);

}
