package org.goblinframework.core.monitor;

import org.goblinframework.api.common.Lifecycle;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface Instruction extends Lifecycle, AutoCloseable {

  Id id();

  Mode mode();

  Instant startTime();

  Instant stopTime();

  Instant completeTime();

  long actualDurationMillis();

  long effectiveDurationMillis();

  @Override
  void start();

  @Override
  void stop();

  void complete();

  @NotNull
  String asLongText();

  @NotNull
  String asShortText();

  @Override
  void close();

  @Override
  default boolean isRunning() {
    throw new UnsupportedOperationException();
  }

  enum Id {
    DOT
  }

  enum Mode {
    DOT,
    ASY,
    SYN
  }
}
