package org.goblinframework.core.util;

public class GoblinNetworkException extends RuntimeException {
  private static final long serialVersionUID = 2985411115497915721L;

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
