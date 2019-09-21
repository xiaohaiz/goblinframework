package org.goblinframework.core.monitor;

import org.goblinframework.core.event.EventBus;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

abstract public class InstructionImpl implements Instruction {

  private final Id id;
  private final Mode mode;

  private Instant startTime;
  private Instant stopTime;
  private Instant completeTime;

  public InstructionImpl(@NotNull Id id, @NotNull Mode mode, boolean autoStart) {
    if (mode == Mode.DOT) {
      throw new IllegalArgumentException();
    }
    this.id = id;
    this.mode = mode;
    if (autoStart) {
      start();
    }
  }

  @Override
  public Id id() {
    return id;
  }

  @Override
  public Mode mode() {
    return mode;
  }

  @Override
  public Instant startTime() {
    return startTime;
  }

  @Override
  public Instant stopTime() {
    return stopTime;
  }

  @Override
  public Instant completeTime() {
    switch (mode) {
      case SYN:
        return stopTime;
      case ASY:
        return completeTime;
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public long actualDurationMillis() {
    if (startTime == null || stopTime == null) {
      throw new IllegalStateException();
    }
    return stopTime.toEpochMilli() - startTime.toEpochMilli();
  }

  @Override
  public long effectiveDurationMillis() {
    switch (mode) {
      case SYN:
        return actualDurationMillis();
      case ASY: {
        if (startTime == null || completeTime == null) {
          throw new IllegalStateException();
        }
        return completeTime.toEpochMilli() - startTime.toEpochMilli();
      }
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public boolean isStarted() {
    return startTime != null;
  }

  @Override
  public boolean isStopped() {
    return stopTime != null;
  }

  @Override
  public boolean isCompleted() {
    if (mode == Mode.SYN) {
      return isStopped();
    }
    return completeTime != null;
  }

  @Override
  public void start() {
    if (startTime == null) {
      startTime = Instant.now();
      FlightMonitor monitor = FlightRecorder.getFlightMonitor();
      if (monitor != null) {
        monitor.attachFlight(this);
      }
    }
  }

  @Override
  public void stop() {
    if (stopTime == null) {
      stopTime = Instant.now();
      if (mode == Mode.SYN) {
        InstructionEvent event = new InstructionEvent(this);
        EventBus.publish(event);
      }
    }
  }

  @Override
  public void complete() {
    if (mode == Mode.SYN) {
      stop();
      return;
    }
    if (completeTime == null) {
      completeTime = Instant.now();
      InstructionEvent event = new InstructionEvent(this);
      EventBus.publish(event);
    }
  }

  @NotNull
  @Override
  public String asLongText() {
    return null;
  }

  @NotNull
  @Override
  public String asShortText() {
    return null;
  }

  @Override
  public void close() {
    stop();
  }
}
