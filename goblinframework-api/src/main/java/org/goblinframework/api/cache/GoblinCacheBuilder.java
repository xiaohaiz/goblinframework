package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface GoblinCacheBuilder extends CacheNameDecorator {

  @Nullable
  Cache getCache(@NotNull String name);

}
