package org.goblinframework.core.compression;

import org.jetbrains.annotations.NotNull;

public class CompressionException extends RuntimeException {
  private static final long serialVersionUID = -1704098810351612906L;

  public CompressionException(String message, Throwable cause) {
    super(message, cause);
  }

  @NotNull
  public static CompressionException newInstance(@NotNull Throwable cause) {
    return new CompressionException(cause.getMessage(), cause);
  }
}
