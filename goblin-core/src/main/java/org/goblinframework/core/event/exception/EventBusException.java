package org.goblinframework.core.event.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EventBusException extends EventException {
  private static final long serialVersionUID = -7710045073339017579L;

  @Nullable private final EventBossException bossException;
  @NotNull private final Map<Integer, EventWorkerException> workerExceptions;
  private final int taskCount;

  public EventBusException(@Nullable EventBossException bossException,
                           @NotNull Map<Integer, EventWorkerException> workerExceptions,
                           int taskCount) {
    this.bossException = bossException;
    this.workerExceptions = workerExceptions;
    this.taskCount = taskCount;
  }

  @Nullable
  public EventBossException getBossException() {
    return bossException;
  }

  @NotNull
  public Map<Integer, EventWorkerException> getWorkerExceptions() {
    return workerExceptions;
  }

  public int getTaskCount() {
    return taskCount;
  }

  @Override
  public synchronized Throwable getCause() {
    if (bossException != null) {
      return bossException;
    }
    return workerExceptions.values().stream().findFirst().orElse(null);
  }
}
