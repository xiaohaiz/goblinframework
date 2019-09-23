package org.goblinframework.api.registry;

import org.goblinframework.api.common.Internal;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface IRegistryBuilderManager {

  @Nullable
  static IRegistryBuilderManager instance() {
    return RegistryBuilderManagerInstaller.INSTALLED;
  }
}
