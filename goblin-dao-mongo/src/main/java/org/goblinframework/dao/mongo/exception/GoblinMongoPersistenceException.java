package org.goblinframework.dao.mongo.exception;

public class GoblinMongoPersistenceException extends GoblinMongoException {
  private static final long serialVersionUID = -5586723465059360234L;

  public GoblinMongoPersistenceException() {
  }

  public GoblinMongoPersistenceException(String message) {
    super(message);
  }

  public GoblinMongoPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMongoPersistenceException(Throwable cause) {
    super(cause);
  }
}
