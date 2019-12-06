package org.goblinframework.dao.mongo.module.exception;

import org.goblinframework.database.core.GoblinDatabaseException;

public class MongoDaoException extends GoblinDatabaseException {
  private static final long serialVersionUID = 2926658123126584743L;

  public MongoDaoException() {
  }

  public MongoDaoException(String message) {
    super(message);
  }

  public MongoDaoException(String message, Throwable cause) {
    super(message, cause);
  }

  public MongoDaoException(Throwable cause) {
    super(cause);
  }
}
