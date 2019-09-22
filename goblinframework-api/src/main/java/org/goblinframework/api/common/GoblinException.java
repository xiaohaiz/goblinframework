package org.goblinframework.api.common;

public class GoblinException extends RuntimeException {
  private static final long serialVersionUID = 613633310479569231L;

  public GoblinException() {
  }

  public GoblinException(String message) {
    super(message);
  }

  public GoblinException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinException(Throwable cause) {
    super(cause);
  }
}
