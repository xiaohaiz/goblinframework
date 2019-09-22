package org.goblinframework.core.exception;

import org.goblinframework.api.common.GoblinException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeoutException;

public class GoblinTimeoutException extends GoblinException {
  private static final long serialVersionUID = -7343820043978038821L;

  public GoblinTimeoutException(@NotNull TimeoutException cause) {
    super(cause.getMessage(), cause);
  }

}
