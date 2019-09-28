package org.goblinframework.registry.core;

import org.jetbrains.annotations.NotNull;

public interface RegistryStateListener {

  void onStateChanged(@NotNull RegistryState state) throws Exception;

}
