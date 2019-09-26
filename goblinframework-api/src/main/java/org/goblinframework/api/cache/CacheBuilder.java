package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CacheBuilder {

  @Nullable
  Cache cache(@NotNull String name);

}
