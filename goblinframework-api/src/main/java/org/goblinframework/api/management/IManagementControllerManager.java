package org.goblinframework.api.management;

import org.goblinframework.api.common.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface IManagementControllerManager {

  void register(@NotNull Object controller);

  @Nullable
  static IManagementControllerManager instance() {
    return ManagementControllerManagerInstaller.INSTALLED;
  }
}
