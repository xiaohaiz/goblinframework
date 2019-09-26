package org.goblinframework.api.cache;

import org.goblinframework.api.core.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface ICacheBuilderManager {

  @Nullable
  CacheBuilder cacheBuilder(@NotNull CacheSystem system);

  @Nullable
  static ICacheBuilderManager instance() {
    return CacheBuilderManagerInstaller.installed;
  }

}
