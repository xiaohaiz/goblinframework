package org.goblinframework.core.compress;

import org.jetbrains.annotations.NotNull;

public class CompressException extends RuntimeException {
  private static final long serialVersionUID = -1704098810351612906L;

  public CompressException(String message, Throwable cause) {
    super(message, cause);
  }

  @NotNull
  public static CompressException newInstance(@NotNull Throwable cause) {
    return new CompressException(cause.getMessage(), cause);
  }
}
