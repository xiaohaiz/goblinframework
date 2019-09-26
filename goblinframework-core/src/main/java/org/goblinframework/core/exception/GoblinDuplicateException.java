package org.goblinframework.core.exception;

import org.goblinframework.api.core.GoblinException;

public class GoblinDuplicateException extends GoblinException {
  private static final long serialVersionUID = 7065031069892083886L;

  public GoblinDuplicateException() {
  }

  public GoblinDuplicateException(String message) {
    super(message);
  }

  public GoblinDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinDuplicateException(Throwable cause) {
    super(cause);
  }
}
