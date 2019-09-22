package org.goblinframework.dao.mysql.module;

import org.goblinframework.api.common.GoblinException;

public class GoblinPersistenceException extends GoblinException {
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
