package org.goblinframework.core.event.exception;

import org.goblinframework.api.core.GoblinException;

abstract public class EventException extends GoblinException {
  private static final long serialVersionUID = -7402036348887345236L;

  public EventException() {
  }

  public EventException(String message) {
    super(message);
  }

  public EventException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventException(Throwable cause) {
    super(cause);
  }
}
