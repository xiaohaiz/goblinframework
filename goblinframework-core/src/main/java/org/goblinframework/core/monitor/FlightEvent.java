package org.goblinframework.core.monitor;

import org.goblinframework.api.event.GoblinEvent;
import org.goblinframework.api.event.GoblinEventChannel;
import org.goblinframework.api.monitor.Flight;
import org.jetbrains.annotations.NotNull;

@GoblinEventChannel("/goblin/monitor")
public class FlightEvent extends GoblinEvent {

  private final Flight flight;

  public FlightEvent(@NotNull Flight flight) {
    this.flight = flight;
  }

  @NotNull
  public Flight getFlight() {
    return flight;
  }
}
