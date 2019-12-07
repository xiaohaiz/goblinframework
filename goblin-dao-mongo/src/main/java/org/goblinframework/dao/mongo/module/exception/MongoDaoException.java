package org.goblinframework.dao.mongo.module.exception;

import org.goblinframework.dao.core.exception.GoblinDaoException;

public class MongoDaoException extends GoblinDaoException {
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
