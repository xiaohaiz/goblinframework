package org.goblinframework.core.module.spi;

import org.goblinframework.core.cache.GoblinCacheBuilder;
import org.goblinframework.core.cache.GoblinCacheSystem;
import org.jetbrains.annotations.NotNull;

public interface RegisterGoblinCacheBuilder {

  void register(@NotNull GoblinCacheSystem system, @NotNull GoblinCacheBuilder builder);

}
