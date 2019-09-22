package org.goblinframework.core.reflection;

import org.goblinframework.api.common.GoblinException;

public class GoblinReflectionException extends GoblinException {
  private static final long serialVersionUID = -3704257696280083921L;

  public GoblinReflectionException() {
  }

  public GoblinReflectionException(String message) {
    super(message);
  }

  public GoblinReflectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinReflectionException(Throwable cause) {
    super(cause);
  }
}
