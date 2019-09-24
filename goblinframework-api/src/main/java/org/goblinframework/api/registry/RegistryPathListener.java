package org.goblinframework.api.registry;

import org.goblinframework.api.common.Block1;
import org.goblinframework.api.common.Disposable;
import org.goblinframework.api.common.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RegistryPathListener extends Initializable, Disposable {

  @NotNull
  RegistryPathListener path(@NotNull String path);

  @NotNull
  RegistryPathListener reload(long time, @NotNull TimeUnit unit);

  @NotNull
  RegistryPathListener handler(Block1<List<String>> handler);

}
