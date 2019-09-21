package org.goblinframework.monitor.flight;

import org.goblinframework.core.monitor.FlightLocation;
import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

final public class Flight implements org.goblinframework.core.monitor.Flight {

  private final FlightId flightId;
  private final FlightLocation location;
  private final Instant startTime;
  private Instant stopTime;
  private final AtomicLong lastDotTime;
  private final FlightInvocationList instructions = new FlightInvocationList();

  Flight(@NotNull FlightId flightId, @NotNull FlightLocation location) {
    this.flightId = flightId;
    this.location = location;
    this.startTime = Instant.now();
    this.lastDotTime = new AtomicLong(this.startTime.toEpochMilli());
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

  @NotNull
  @Override
  public Instant startTime() {
    return startTime;
  }

  @Nullable
  @Override
  public Instant stopTime() {
    return stopTime;
  }

  public long updateDot(long millis) {
    long previous = lastDotTime.getAndSet(millis);
    return millis - previous;
  }

  public FlightInvocationList getInstructions() {
    return instructions;
  }

  void stop() {
    if (stopTime != null) {
      throw new IllegalStateException();
    }
    stopTime = Instant.now();
  }

  void addInstruction(@NotNull Instruction instruction) {
    instructions.add(instruction);
  }
}
