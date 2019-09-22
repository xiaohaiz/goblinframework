package org.goblinframework.core.cache;

import org.jetbrains.annotations.NotNull;

public interface GoblinCacheNameDecorator {

  @NotNull
  default String decorateCacheName(@NotNull String name) {
    return name;
  }
}
