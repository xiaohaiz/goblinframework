package org.goblinframework.registry.listener;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface RegistryChildListener {

  default void onParentPathDeleted(@NotNull String parentPath) throws Exception {
    onChildChanged(parentPath, Collections.emptyList());
  }

  void onChildChanged(@NotNull String parentPath, @NotNull List<String> children) throws Exception;

}
