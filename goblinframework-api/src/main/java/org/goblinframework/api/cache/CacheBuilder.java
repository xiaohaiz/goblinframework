package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CacheBuilder extends CacheNameDecorator {

  @Nullable
  Cache cache(@NotNull String name);

}
