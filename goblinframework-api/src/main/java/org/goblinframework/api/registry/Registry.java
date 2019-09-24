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

  void subscribeChildListener(@NotNull String path, @NotNull RegistryChildListener listener);

  void unsubscribeChildListener(@NotNull String path, @NotNull RegistryChildListener listener);

  void subscribeDataListener(@NotNull String path, @NotNull RegistryDataListener listener);

  void unsubscribeDataListener(@NotNull String path, @NotNull RegistryDataListener listener);

  void subscribeStateListener(@NotNull RegistryStateListener listener);

  void unsubscribeStateListener(@NotNull RegistryStateListener listener);

  @NotNull
  RegistryPathListener createPathListener();
}
