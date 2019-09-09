package org.goblinframework.api.common;

public class DuplicateException extends GoblinException {
  private static final long serialVersionUID = 7065031069892083886L;

  public DuplicateException() {
  }

  public DuplicateException(String message) {
    super(message);
  }

  public DuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateException(Throwable cause) {
    super(cause);
  }
}
