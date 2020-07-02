package org.goblinframework.dao.exception;

import org.goblinframework.api.core.GoblinException;

public class GoblinDaoException extends GoblinException {
  private static final long serialVersionUID = 3178592068082185480L;

  public GoblinDaoException() {
  }

  public GoblinDaoException(String message) {
    super(message);
  }

  public GoblinDaoException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinDaoException(Throwable cause) {
    super(cause);
  }
}
