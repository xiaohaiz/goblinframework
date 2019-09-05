package org.goblinframework.api.concurrent;

import java.util.concurrent.TimeoutException;

public class GoblinTimeoutException extends RuntimeException {
  private static final long serialVersionUID = -7343820043978038821L;

  public GoblinTimeoutException(TimeoutException cause) {
    super(cause.getMessage(), cause);
  }

}
