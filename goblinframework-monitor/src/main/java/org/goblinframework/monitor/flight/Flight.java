package org.goblinframework.monitor.flight;

import org.goblinframework.api.common.ReferenceCount;
import org.goblinframework.core.monitor.FlightLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

final public class Flight implements org.goblinframework.core.monitor.Flight, ReferenceCount {

  @NotNull
  @Override
  public String flightId() {
    return null;
  }

  @NotNull
  @Override
  public FlightLocation location() {
    return null;
  }

  private final AtomicInteger referenceCount = new AtomicInteger();

  @Override
  public int count() {
    return referenceCount.get();
  }

  @Override
  public void retain(int increment) {

  }

  @Override
  public boolean release(int decrement) {
    return false;
  }
}
