package org.goblinframework.core.exception;

import org.goblinframework.api.common.GoblinException;

public class GoblinTranscodingException extends GoblinException {
  private static final long serialVersionUID = -480279730643006921L;

  public GoblinTranscodingException() {
  }

  public GoblinTranscodingException(String message) {
    super(message);
  }

  public GoblinTranscodingException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinTranscodingException(Throwable cause) {
    super(cause);
  }
}
