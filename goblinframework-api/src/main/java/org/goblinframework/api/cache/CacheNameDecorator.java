package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;

public interface CacheNameDecorator {

  @NotNull
  default String decorateCacheName(@NotNull String name) {
    return name;
  }
}
