package org.goblinframework.queue;

import org.goblinframework.api.core.GoblinException;

public class GoblinQueueException extends GoblinException {
  private static final long serialVersionUID = -4783276769169355828L;

  public GoblinQueueException() {
  }

  public GoblinQueueException(String message) {
    super(message);
  }

  public GoblinQueueException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinQueueException(Throwable cause) {
    super(cause);
  }
}
