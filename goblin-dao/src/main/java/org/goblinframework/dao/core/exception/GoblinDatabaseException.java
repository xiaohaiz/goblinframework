package org.goblinframework.dao.core.exception;

import org.goblinframework.api.core.GoblinException;

public class GoblinDatabaseException extends GoblinException {
  private static final long serialVersionUID = 3178592068082185480L;

  public GoblinDatabaseException() {
  }

  public GoblinDatabaseException(String message) {
    super(message);
  }

  public GoblinDatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinDatabaseException(Throwable cause) {
    super(cause);
  }
}
