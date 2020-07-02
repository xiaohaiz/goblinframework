package org.goblinframework.dao.mongo.exception;

import org.goblinframework.dao.exception.GoblinDaoException;

abstract public class GoblinMongoException extends GoblinDaoException {
  private static final long serialVersionUID = 2926658123126584743L;

  public GoblinMongoException() {
  }

  public GoblinMongoException(String message) {
    super(message);
  }

  public GoblinMongoException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMongoException(Throwable cause) {
    super(cause);
  }
}
