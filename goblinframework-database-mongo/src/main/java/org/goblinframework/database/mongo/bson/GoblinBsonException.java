package org.goblinframework.database.mongo.bson;

import org.goblinframework.api.core.GoblinException;

public class GoblinBsonException extends GoblinException {
  private static final long serialVersionUID = -3118075477565475327L;

  public GoblinBsonException() {
  }

  public GoblinBsonException(String message) {
    super(message);
  }

  public GoblinBsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinBsonException(Throwable cause) {
    super(cause);
  }
}
