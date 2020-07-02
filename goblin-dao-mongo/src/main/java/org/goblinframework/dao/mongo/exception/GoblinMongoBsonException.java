package org.goblinframework.dao.mongo.exception;

public class GoblinMongoBsonException extends GoblinMongoException {
  private static final long serialVersionUID = -3118075477565475327L;

  public GoblinMongoBsonException() {
  }

  public GoblinMongoBsonException(String message) {
    super(message);
  }

  public GoblinMongoBsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMongoBsonException(Throwable cause) {
    super(cause);
  }
}
