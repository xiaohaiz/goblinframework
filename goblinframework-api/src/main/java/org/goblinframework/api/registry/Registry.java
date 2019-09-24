package org.goblinframework.api.registry;

import org.jetbrains.annotations.NotNull;

public interface Registry {

  @NotNull
  RegistryLocation location();

  @NotNull
  default RegistrySystem system() {
    return location().system;
  }

  @NotNull
  default String name() {
    return location().name;
  }

  void subscribeStateListener(@NotNull RegistryStateListener listener);

  void unsubscribeStateListener(@NotNull RegistryStateListener listener);

}
