package org.goblinframework.core.exception;

public class GoblinNetworkException extends GoblinException {
  private static final long serialVersionUID = 2387665840385166671L;

  public GoblinNetworkException() {
  }

  public GoblinNetworkException(String message) {
    super(message);
  }

  public GoblinNetworkException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinNetworkException(Throwable cause) {
    super(cause);
  }
}
