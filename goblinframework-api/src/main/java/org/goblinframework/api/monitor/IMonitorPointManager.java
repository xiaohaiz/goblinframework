package org.goblinframework.api.monitor;

import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMonitorPointManager {

  void register(@NotNull MonitorPoint monitorPoint);

  @Nullable
  static IMonitorPointManager instance() {
    return ServiceInstaller.firstOrNull(IMonitorPointManager.class);
  }
}
