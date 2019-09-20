package org.goblinframework.monitor.flight;

import org.goblinframework.api.common.ReferenceCount;
import org.goblinframework.core.monitor.FlightLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

final public class Flight implements org.goblinframework.core.monitor.Flight, ReferenceCount {

  private final String flightId;
  private final FlightLocation location;

  Flight(@NotNull String flightId, @NotNull FlightLocation location) {
    this.flightId = flightId;
    this.location = location;
  }

  @NotNull
  @Override
  public String flightId() {
    return flightId;
  }

  @NotNull
  @Override
  public FlightLocation location() {
    return location;
  }

  // ==========================================================================
  // org.goblinframework.api.common.ReferenceCount supported
  // ==========================================================================

  private final AtomicInteger referenceCount = new AtomicInteger();

  @Override
  public int count() {
    return referenceCount.get();
  }

  @Override
  public void retain(int increment) {
    referenceCount.addAndGet(increment);
  }

  @Override
  public boolean release(int decrement) {
    return referenceCount.addAndGet(-decrement) <= 0;
  }
}
