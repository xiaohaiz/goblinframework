package org.goblinframework.core.exception;

public class GoblinSerializationException extends GoblinException {
  private static final long serialVersionUID = 6366045869964789428L;

  public GoblinSerializationException() {
  }

  public GoblinSerializationException(String message) {
    super(message);
  }

  public GoblinSerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinSerializationException(Throwable cause) {
    super(cause);
  }
}
