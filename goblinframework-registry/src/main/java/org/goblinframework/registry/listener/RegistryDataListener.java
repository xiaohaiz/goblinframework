package org.goblinframework.registry.listener;

import org.jetbrains.annotations.NotNull;

public interface RegistryDataListener {

  void onDataChanged(@NotNull String dataPath, @NotNull Object data) throws Exception;

  void onDataDeleted(@NotNull String dataPath) throws Exception;

}
