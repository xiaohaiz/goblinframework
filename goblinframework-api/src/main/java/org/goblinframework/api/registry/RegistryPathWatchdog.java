package org.goblinframework.api.registry;

import org.goblinframework.api.core.Disposable;
import org.goblinframework.api.core.Initializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public interface RegistryPathWatchdog extends Initializable, Disposable {

  RegistryPathWatchdog scheduler(long time, @NotNull TimeUnit unit);

  void watch(@NotNull String path, boolean ephemeral, @Nullable Object data);

  void unwatch(@NotNull String path);

}
