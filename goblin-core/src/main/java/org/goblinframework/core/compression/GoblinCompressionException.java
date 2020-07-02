package org.goblinframework.core.compression;

import org.goblinframework.api.core.GoblinException;

public class GoblinCompressionException extends GoblinException {
  private static final long serialVersionUID = -1704098810351612906L;

  public GoblinCompressionException() {
  }

  public GoblinCompressionException(String message) {
    super(message);
  }

  public GoblinCompressionException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinCompressionException(Throwable cause) {
    super(cause);
  }
}
