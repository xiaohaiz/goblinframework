package org.goblinframework.registry.module.exception;

import org.goblinframework.api.core.GoblinException;

public class RegistryException extends GoblinException {
  private static final long serialVersionUID = 1643837809853171010L;

  public RegistryException() {
  }

  public RegistryException(String message) {
    super(message);
  }

  public RegistryException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegistryException(Throwable cause) {
    super(cause);
  }
}
