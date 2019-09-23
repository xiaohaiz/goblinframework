package org.goblinframework.monitor.module.monitor.instruction;

import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.api.monitor.InstructionTranslator;
import org.goblinframework.core.monitor.Flight;
import org.goblinframework.core.util.StringUtils;
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
  String dotName() {
    return dotName;
  }

  @NotNull
  Instant dotTime() {
    return dotTime;
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
    throw new UnsupportedOperationException();
  }

  @Override
  public Instant stopTime() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Instant completeTime() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long actualDurationMillis() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long effectiveDurationMillis() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isStarted() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isStopped() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isCompleted() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void start() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void complete() {
    throw new UnsupportedOperationException();
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

  @NotNull
  @Override
  public InstructionTranslator translator() {
    return pretty -> {
      if (!(flight instanceof org.goblinframework.monitor.flight.Flight)) {
        return StringUtils.EMPTY;
      }
      long millis = dotTime().toEpochMilli();
      long delta = ((org.goblinframework.monitor.flight.Flight) flight).updateDot(millis);
      if (pretty) {
        return String.format("+%sms %s", delta, dotName());
      } else {
        return String.format("(%s)+%dms", dotName(), delta);
      }
    };
  }

  @Override
  public void close() {
  }
}
