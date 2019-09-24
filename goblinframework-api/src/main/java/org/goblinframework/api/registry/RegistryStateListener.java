package org.goblinframework.api.registry;

import org.jetbrains.annotations.NotNull;

public interface RegistryStateListener {

  void onStateChanged(@NotNull RegistryState state);

}
