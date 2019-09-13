package org.goblinframework.core.exception;

import org.jetbrains.annotations.NotNull;

public class GoblinCompressionException extends GoblinException {
  private static final long serialVersionUID = -1704098810351612906L;

  public GoblinCompressionException(String message, Throwable cause) {
    super(message, cause);
  }

  @NotNull
  public static GoblinCompressionException newInstance(@NotNull Throwable cause) {
    return new GoblinCompressionException(cause.getMessage(), cause);
  }
}
