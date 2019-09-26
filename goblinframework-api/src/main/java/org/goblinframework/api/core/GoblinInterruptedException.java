package org.goblinframework.api.core;

import org.jetbrains.annotations.NotNull;

public class GoblinInterruptedException extends GoblinException {
  private static final long serialVersionUID = -6869879530032524615L;

  public GoblinInterruptedException(@NotNull InterruptedException cause) {
    super(cause.getMessage(), cause);
  }
}
