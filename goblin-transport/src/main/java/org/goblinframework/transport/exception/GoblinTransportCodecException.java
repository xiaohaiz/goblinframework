package org.goblinframework.transport.exception;

public class GoblinTransportCodecException extends GoblinTransportException {
  private static final long serialVersionUID = 7622251455025410317L;

  public GoblinTransportCodecException() {
  }

  public GoblinTransportCodecException(String message) {
    super(message);
  }

  public GoblinTransportCodecException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinTransportCodecException(Throwable cause) {
    super(cause);
  }
}
