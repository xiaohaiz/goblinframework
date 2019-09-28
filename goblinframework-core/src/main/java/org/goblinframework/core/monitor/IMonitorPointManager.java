package org.goblinframework.core.monitor;

import org.goblinframework.core.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMonitorPointManager {

  void register(@NotNull MonitorPoint monitorPoint);

  @Nullable
  static IMonitorPointManager instance() {
    return ServiceInstaller.firstOrNull(IMonitorPointManager.class);
  }
}
