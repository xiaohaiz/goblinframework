package org.goblinframework.core.exception;

import org.goblinframework.api.core.GoblinException;

public class GoblinMappingException extends GoblinException {
  private static final long serialVersionUID = 7894253609185404466L;

  public GoblinMappingException() {
  }

  public GoblinMappingException(String message) {
    super(message);
  }

  public GoblinMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinMappingException(Throwable cause) {
    super(cause);
  }
}
