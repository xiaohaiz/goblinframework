package org.goblinframework.core.monitor;

import org.jetbrains.annotations.NotNull;

public interface Flight {

  @NotNull
  FlightId flightId();

  @NotNull
  default StartPoint startPoint() {
    return location().startPoint();
  }

  @NotNull
  FlightLocation location();

  interface Aware {

    void setFlight(@NotNull Flight flight);

  }
}
