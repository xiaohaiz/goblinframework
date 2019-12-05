package org.goblinframework.transport.core.codec;

import org.goblinframework.api.core.GoblinException;

public class TransportCodecException extends GoblinException {
  private static final long serialVersionUID = 7622251455025410317L;

  public TransportCodecException() {
  }

  public TransportCodecException(String message) {
    super(message);
  }

  public TransportCodecException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransportCodecException(Throwable cause) {
    super(cause);
  }
}
