package org.goblinframework.core.exception;

public class GoblinInitializeException extends GoblinException {
  private static final long serialVersionUID = 8326071781689456335L;

  public GoblinInitializeException() {
  }

  public GoblinInitializeException(String message) {
    super(message);
  }

  public GoblinInitializeException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinInitializeException(Throwable cause) {
    super(cause);
  }
}
