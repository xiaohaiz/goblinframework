package org.goblinframework.monitor.flight;

import org.goblinframework.core.monitor.Flight;
import org.goblinframework.core.monitor.FlightLocation;
import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

final public class FlightImpl implements Flight {

  private final FlightIdImpl flightId;
  private final FlightLocation location;
  private final String threadName;
  private final Instant startTime;
  private Instant stopTime;
  private final AtomicLong lastDotTime;
  private final FlightInvocationList instructions = new FlightInvocationList();

  FlightImpl(@NotNull FlightIdImpl flightId, @NotNull FlightLocation location) {
    this.flightId = flightId;
    this.location = location;
    this.threadName = Thread.currentThread().getName();
    this.startTime = Instant.now();
    this.lastDotTime = new AtomicLong(this.startTime.toEpochMilli());
  }

  @NotNull
  public String getThreadName() {
    return threadName;
  }

  @NotNull
  @Override
  public FlightIdImpl flightId() {
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

  @Nullable
  @Override
  public Object attribute(@NotNull String name) {
    return location.attribute(name);
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
