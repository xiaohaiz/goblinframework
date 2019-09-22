package org.goblinframework.core.module.spi;

import org.goblinframework.core.cache.GoblinCacheBuilder;
import org.jetbrains.annotations.NotNull;

public interface RegisterGoblinCacheBuilder {

  void register(@NotNull GoblinCacheBuilder builder);

}
