package org.goblinframework.core.monitor;

import org.goblinframework.api.core.Lifecycle;
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

  boolean isStarted();

  boolean isStopped();

  boolean isCompleted();

  @Override
  void start();

  @Override
  void stop();

  void complete();

  @NotNull
  String asLongText();

  @NotNull
  String asShortText();

  @NotNull
  default InstructionTranslator translator() {
    return pretty -> pretty ? asLongText() : asShortText();
  }

  @Override
  void close();

  @Override
  default boolean isRunning() {
    throw new UnsupportedOperationException();
  }

  enum Id {
    DOT,
    RDS,
    VMC,
    MNG,
    MSQ,
    ZKP,
    RIC,
    RIS,
    PMG,
  }

  enum Mode {
    DOT,
    ASY,
    SYN
  }
}
