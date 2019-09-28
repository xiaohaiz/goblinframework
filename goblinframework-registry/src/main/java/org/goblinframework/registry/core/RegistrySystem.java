package org.goblinframework.registry.core;

import org.goblinframework.registry.core.manager.RegistryBuilderManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum RegistrySystem {

  ZKP;

  @Nullable
  public RegistryBuilder getRegistryBuilder() {
    return RegistryBuilderManager.INSTANCE.getRegistryBuilder(this);
  }

  @Nullable
  public Registry getRegistry(@NotNull String name) {
    RegistryBuilder builder = getRegistryBuilder();
    return builder == null ? null : builder.getRegistry(name);
  }

}
