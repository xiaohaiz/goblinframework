package org.goblinframework.core.exception;

public class GoblinMalformedException extends GoblinException {
  private static final long serialVersionUID = 8720083964412642204L;

  public GoblinMalformedException() {
  }

  public GoblinMalformedException(String message) {
    super(message);
  }

  public GoblinMalformedException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMalformedException(Throwable cause) {
    super(cause);
  }
}
