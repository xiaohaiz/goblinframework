package org.goblinframework.core.module.spi;

import org.goblinframework.api.monitor.MonitorPoint;
import org.jetbrains.annotations.NotNull;

@Deprecated
public interface RegisterMonitorPoint {

  void register(@NotNull MonitorPoint monitorPoint);

}
