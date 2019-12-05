package org.goblinframework.core.event.exception;

abstract public class EventWorkerException extends EventException {
  private static final long serialVersionUID = -8019897228910883892L;

  public EventWorkerException() {
  }

  public EventWorkerException(String message) {
    super(message);
  }

  public EventWorkerException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventWorkerException(Throwable cause) {
    super(cause);
  }
}
