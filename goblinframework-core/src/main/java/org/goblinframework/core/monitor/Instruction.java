package org.goblinframework.core.monitor;

import org.goblinframework.api.common.Lifecycle;

import java.time.Instant;

public interface Instruction extends Lifecycle, AutoCloseable {

  Id id();

  Instant startTime();

  Instant stopTime();

  Instant completeTime();

  @Override
  void close();

  @Override
  default boolean isRunning() {
    throw new UnsupportedOperationException();
  }

  enum Id {

  }
}
