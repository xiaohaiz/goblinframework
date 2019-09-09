package org.goblinframework.api.common;

public class InitializeException extends GoblinException {
  private static final long serialVersionUID = 8326071781689456335L;

  public InitializeException() {
  }

  public InitializeException(String message) {
    super(message);
  }

  public InitializeException(String message, Throwable cause) {
    super(message, cause);
  }

  public InitializeException(Throwable cause) {
    super(cause);
  }
}
