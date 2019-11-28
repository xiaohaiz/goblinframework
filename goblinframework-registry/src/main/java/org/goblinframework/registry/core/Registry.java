package org.goblinframework.registry.core;

import org.goblinframework.registry.listener.RegistryChildListener;
import org.goblinframework.registry.listener.RegistryDataListener;
import org.goblinframework.registry.listener.RegistryStateListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

  boolean exists(@NotNull String path);

  @NotNull
  List<String> getChildren(@NotNull String path);

  @Nullable
  <E> E readData(@NotNull String path);

  void writeData(@NotNull String path, @Nullable Object data);

  void createPersistent(@NotNull String path);

  void createPersistent(@NotNull String path, @Nullable Object data);

  void createEphemeral(@NotNull String path);

  void createEphemeral(@NotNull String path, @Nullable Object data);

  boolean delete(@NotNull String path);

  boolean deleteRecursive(@NotNull String path);

  void subscribeChildListener(@NotNull String path, @NotNull RegistryChildListener listener);

  void unsubscribeChildListener(@NotNull String path, @NotNull RegistryChildListener listener);

  void subscribeDataListener(@NotNull String path, @NotNull RegistryDataListener listener);

  void unsubscribeDataListener(@NotNull String path, @NotNull RegistryDataListener listener);

  void subscribeStateListener(@NotNull RegistryStateListener listener);

  void unsubscribeStateListener(@NotNull RegistryStateListener listener);

  @NotNull
  RegistryPathListener createPathListener();

  @NotNull
  RegistryPathWatchdog createPathWatchdog();
}
