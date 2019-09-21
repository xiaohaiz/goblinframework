package org.goblinframework.monitor.module.monitor.instruction;

import org.goblinframework.core.monitor.Flight;
import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

final public class DOT implements Instruction, Flight.Aware {

  private final String dotName;
  private final Instant dotTime;
  private Flight flight;

  public DOT(String dotName) {
    this.dotName = dotName;
    this.dotTime = Instant.now();
  }

  @Nullable
  public String getDotName() {
    return dotName;
  }

  @Nullable
  public Flight getFlight() {
    return flight;
  }

  @Override
  public void setFlight(@NotNull Flight flight) {
    this.flight = flight;
  }

  @Override
  public Id id() {
    return Id.DOT;
  }

  @Override
  public Mode mode() {
    return Mode.DOT;
  }

  @Override
  public Instant startTime() {
    return dotTime;
  }

  @Override
  public Instant stopTime() {
    return dotTime;
  }

  @Override
  public Instant completeTime() {
    return dotTime;
  }

  @Override
  public long actualDurationMillis() {
    return 0;
  }

  @Override
  public long effectiveDurationMillis() {
    return 0;
  }

  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @Override
  public void complete() {
  }

  @NotNull
  @Override
  public String asLongText() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public String asShortText() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
  }
}
