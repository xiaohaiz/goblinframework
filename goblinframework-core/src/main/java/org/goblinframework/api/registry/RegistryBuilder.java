package org.goblinframework.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RegistryBuilder {

  @Nullable
  Registry getRegistry(@NotNull String name);

}
