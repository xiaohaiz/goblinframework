package org.goblinframework.api.monitor;

import org.jetbrains.annotations.NotNull;

public interface MonitorPoint {

  @NotNull
  MonitorPointId id();

  @NotNull
  Mode mode();

  enum Mode {
    COUNT,
    INVOCATION,
    SNAPSHOT
  }
}
