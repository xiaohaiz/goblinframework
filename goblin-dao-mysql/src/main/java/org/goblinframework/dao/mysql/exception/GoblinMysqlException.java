package org.goblinframework.dao.mysql.exception;

import org.goblinframework.dao.exception.GoblinDaoException;

abstract public class GoblinMysqlException extends GoblinDaoException {
  private static final long serialVersionUID = -1992975964812164011L;

  public GoblinMysqlException() {
  }

  public GoblinMysqlException(String message) {
    super(message);
  }

  public GoblinMysqlException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMysqlException(Throwable cause) {
    super(cause);
  }
}
