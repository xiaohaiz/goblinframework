package org.goblinframework.api.management;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public interface IManagementControllerManager {

  void register(@NotNull Object controller);

  @Nullable
  static IManagementControllerManager instance() {
    return ServiceInstaller.firstOrNull(IManagementControllerManager.class);
  }
}
