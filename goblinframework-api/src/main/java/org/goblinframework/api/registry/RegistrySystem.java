package org.goblinframework.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum RegistrySystem {

  ZKP;

  @Nullable
  public RegistryBuilder getRegistryBuilder() {
    IRegistryBuilderManager builderManager = IRegistryBuilderManager.instance();
    return builderManager == null ? null : builderManager.getRegistryBuilder(this);
  }

  @Nullable
  public Registry getRegistry(@NotNull String name) {
    RegistryBuilder builder = getRegistryBuilder();
    return builder == null ? null : builder.getRegistry(name);
  }

}
