package org.goblinframework.core.module.spi;

import org.goblinframework.api.cache.GoblinCacheBuilder;
import org.goblinframework.api.cache.CacheSystem;
import org.jetbrains.annotations.NotNull;

public interface RegisterGoblinCacheBuilder {

  void register(@NotNull CacheSystem system, @NotNull GoblinCacheBuilder builder);

}
