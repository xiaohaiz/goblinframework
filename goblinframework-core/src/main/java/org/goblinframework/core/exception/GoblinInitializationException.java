package org.goblinframework.core.exception;

import org.goblinframework.api.common.GoblinException;

public class GoblinInitializationException extends GoblinException {
  private static final long serialVersionUID = 8326071781689456335L;

  public GoblinInitializationException() {
  }

  public GoblinInitializationException(String message) {
    super(message);
  }

  public GoblinInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinInitializationException(Throwable cause) {
    super(cause);
  }
}
