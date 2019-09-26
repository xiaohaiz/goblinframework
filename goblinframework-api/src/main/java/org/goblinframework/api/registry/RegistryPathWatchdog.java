package org.goblinframework.api.registry;

import org.goblinframework.api.common.Disposable;
import org.goblinframework.api.common.Initializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RegistryPathWatchdog extends Initializable, Disposable {

  void watch(@NotNull String path, boolean ephemeral, @Nullable Object data);

  void unwatch(@NotNull String path);

}
