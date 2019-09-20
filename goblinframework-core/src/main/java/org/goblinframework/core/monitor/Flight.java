package org.goblinframework.core.monitor;

import org.jetbrains.annotations.NotNull;

public interface Flight {

  @NotNull
  String flightId();

  @NotNull
  default StartPoint startPoint() {
    return location().startPoint();
  }

  @NotNull
  FlightLocation location();
}
