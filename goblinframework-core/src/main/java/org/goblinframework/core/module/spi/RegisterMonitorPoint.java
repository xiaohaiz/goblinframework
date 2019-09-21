package org.goblinframework.core.module.spi;

import org.goblinframework.core.monitor.MonitorPoint;
import org.jetbrains.annotations.NotNull;

public interface RegisterMonitorPoint {

  void register(@NotNull MonitorPoint monitorPoint);

}
