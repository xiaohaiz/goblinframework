package org.goblinframework.core.util;

public class GoblinMappingException extends RuntimeException {
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
