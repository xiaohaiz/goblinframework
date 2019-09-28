package org.goblinframework.core.schedule;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.core.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface ICronTaskManager {

  void register(@NotNull CronTask task);

  void unregister(@NotNull String name);

  @Nullable
  static ICronTaskManager instance() {
    return ServiceInstaller.firstOrNull(ICronTaskManager.class);
  }
}
