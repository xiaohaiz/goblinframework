package org.goblinframework.core.system;

import org.goblinframework.api.core.GoblinException;

public class GoblinSystemException extends GoblinException {
  private static final long serialVersionUID = -7782095114583803355L;

  public GoblinSystemException() {
  }

  public GoblinSystemException(String message) {
    super(message);
  }

  public GoblinSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinSystemException(Throwable cause) {
    super(cause);
  }
}
