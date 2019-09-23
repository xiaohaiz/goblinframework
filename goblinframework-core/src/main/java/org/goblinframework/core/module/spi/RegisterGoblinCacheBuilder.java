package org.goblinframework.core.module.spi;

import org.goblinframework.api.cache.GoblinCacheBuilder;
import org.goblinframework.api.cache.GoblinCacheSystem;
import org.jetbrains.annotations.NotNull;

public interface RegisterGoblinCacheBuilder {

  void register(@NotNull GoblinCacheSystem system, @NotNull GoblinCacheBuilder builder);

}
