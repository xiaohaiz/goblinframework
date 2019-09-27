package org.goblinframework.core.transcoder;

import org.goblinframework.api.core.GoblinException;

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
}
