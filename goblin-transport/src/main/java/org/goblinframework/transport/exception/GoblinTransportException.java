package org.goblinframework.transport.exception;

import org.goblinframework.api.core.GoblinException;

public class GoblinTransportException extends GoblinException {
  private static final long serialVersionUID = -870821289076972697L;

  public GoblinTransportException() {
  }

  public GoblinTransportException(String message) {
    super(message);
  }

  public GoblinTransportException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinTransportException(Throwable cause) {
    super(cause);
  }
}
