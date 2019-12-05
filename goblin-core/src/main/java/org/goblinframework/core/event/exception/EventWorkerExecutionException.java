package org.goblinframework.core.event.exception;

import org.jetbrains.annotations.NotNull;

public class EventWorkerExecutionException extends EventWorkerException {
  private static final long serialVersionUID = -4431105332985870565L;

  public EventWorkerExecutionException(@NotNull Throwable cause) {
    super(cause);
  }

  @NotNull
  public static EventWorkerExecutionException rethrow(@NotNull Throwable cause) {
    if (cause instanceof EventWorkerExecutionException) {
      return (EventWorkerExecutionException) cause;
    }
    return new EventWorkerExecutionException(cause);
  }
}
