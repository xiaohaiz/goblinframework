package org.goblinframework.monitor.flight;

import org.goblinframework.core.monitor.FlightLocation;
import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;

final public class Flight implements org.goblinframework.core.monitor.Flight {

  private final FlightId flightId;
  private final FlightLocation location;

  Flight(@NotNull FlightId flightId, @NotNull FlightLocation location) {
    this.flightId = flightId;
    this.location = location;
  }

  @NotNull
  @Override
  public FlightId flightId() {
    return flightId;
  }

  @NotNull
  @Override
  public FlightLocation location() {
    return location;
  }

  void addInstruction(@NotNull Instruction instruction) {

  }
}
