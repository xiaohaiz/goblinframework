package org.goblinframework.core.event.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EventBusException extends EventException {
  private static final long serialVersionUID = -7710045073339017579L;

  public EventBusException(@Nullable EventBossException bossException,
                           @NotNull Map<Integer, EventWorkerException> workerExceptions,
                           int taskCount) {
  }
}
