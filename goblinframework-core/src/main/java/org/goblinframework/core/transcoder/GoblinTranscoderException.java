package org.goblinframework.core.transcoder;

import org.goblinframework.api.core.GoblinException;
import org.jetbrains.annotations.NotNull;

public class GoblinTranscoderException extends GoblinException {
  private static final long serialVersionUID = -480279730643006921L;

  public GoblinTranscoderException() {
  }

  public GoblinTranscoderException(String message) {
    super(message);
  }

  public GoblinTranscoderException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinTranscoderException(Throwable cause) {
    super(cause);
  }

  @NotNull
  public static GoblinTranscoderException rethrow(@NotNull Throwable cause) {
    if (cause instanceof GoblinTranscoderException) {
      return (GoblinTranscoderException) cause;
    } else {
      return new GoblinTranscoderException(cause);
    }
  }
}
