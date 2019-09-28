package org.goblinframework.api.registry;

import org.goblinframework.api.annotation.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface IRegistryBuilderManager {

  @Nullable
  RegistryBuilder getRegistryBuilder(@NotNull RegistrySystem system);

  @Nullable
  static IRegistryBuilderManager instance() {
    return RegistryBuilderManagerInstaller.INSTALLED;
  }
}
