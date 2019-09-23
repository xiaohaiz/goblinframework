package org.goblinframework.api.cache;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public interface ICacheBuilderManager {

  void register(@NotNull CacheSystem system, @NotNull CacheBuilder builder);

  @Nullable
  static ICacheBuilderManager instance() {
    return ServiceInstaller.firstOrNull(ICacheBuilderManager.class);
  }

}
