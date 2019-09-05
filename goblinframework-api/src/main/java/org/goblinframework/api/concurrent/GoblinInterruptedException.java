package org.goblinframework.api.concurrent;

public class GoblinInterruptedException extends RuntimeException {
  private static final long serialVersionUID = -6869879530032524615L;

  public GoblinInterruptedException(InterruptedException cause) {
    super(cause.getMessage(), cause);
  }
}
