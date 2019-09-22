package org.goblinframework.core.exception;

import org.goblinframework.api.common.GoblinException;

public class GoblinConfigException extends GoblinException {
  private static final long serialVersionUID = 9129702786634389940L;

  public GoblinConfigException() {
  }

  public GoblinConfigException(String message) {
    super(message);
  }

  public GoblinConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinConfigException(Throwable cause) {
    super(cause);
  }
}
