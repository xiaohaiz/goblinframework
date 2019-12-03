package org.goblinframework.core.event.exception;

abstract public class EventBossException extends EventException {
  private static final long serialVersionUID = 6272465414215587969L;

  public EventBossException() {
  }

  public EventBossException(String message) {
    super(message);
  }

  public EventBossException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventBossException(Throwable cause) {
    super(cause);
  }
}
