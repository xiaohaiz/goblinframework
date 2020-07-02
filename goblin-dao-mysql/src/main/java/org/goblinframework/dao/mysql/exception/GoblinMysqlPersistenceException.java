package org.goblinframework.dao.mysql.exception;

public class GoblinMysqlPersistenceException extends GoblinMysqlException {
  private static final long serialVersionUID = -4708153637213137878L;

  public GoblinMysqlPersistenceException() {
  }

  public GoblinMysqlPersistenceException(String message) {
    super(message);
  }

  public GoblinMysqlPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMysqlPersistenceException(Throwable cause) {
    super(cause);
  }
}
