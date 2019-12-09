package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.exception.GoblinDaoException;

public class GoblinPersistenceException extends GoblinDaoException {
  private static final long serialVersionUID = -1109476785668600971L;

  public GoblinPersistenceException() {
  }

  public GoblinPersistenceException(String message) {
    super(message);
  }

  public GoblinPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinPersistenceException(Throwable cause) {
    super(cause);
  }
}
