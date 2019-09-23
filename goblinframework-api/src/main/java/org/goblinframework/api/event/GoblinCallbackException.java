package org.goblinframework.api.event;

import org.goblinframework.api.common.GoblinException;
import org.jetbrains.annotations.NotNull;

public class GoblinCallbackException extends GoblinException {
  private static final long serialVersionUID = 8739931958023711661L;

  private GoblinCallbackException(Throwable cause) {
    super(cause);
  }

  public static void throwException(@NotNull Throwable cause) {
    if (cause instanceof RuntimeException) {
      throw (RuntimeException) cause;
    }
    throw new GoblinCallbackException(cause);
  }
}
