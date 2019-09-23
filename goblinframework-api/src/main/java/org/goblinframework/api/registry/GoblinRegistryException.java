package org.goblinframework.api.registry;

import org.goblinframework.api.common.GoblinException;

public class GoblinRegistryException extends GoblinException {
  private static final long serialVersionUID = 1643837809853171010L;

  public GoblinRegistryException() {
  }

  public GoblinRegistryException(String message) {
    super(message);
  }

  public GoblinRegistryException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinRegistryException(Throwable cause) {
    super(cause);
  }
}
