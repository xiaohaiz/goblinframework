package org.goblinframework.api.schedule;

import org.goblinframework.api.common.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface ICronTaskManager {

  void register(@NotNull CronTask task);

  void unregister(@NotNull CronTask task);

  @Nullable
  static ICronTaskManager instance() {
    return CronTaskManagerInstaller.INSTALLED;
  }
}
