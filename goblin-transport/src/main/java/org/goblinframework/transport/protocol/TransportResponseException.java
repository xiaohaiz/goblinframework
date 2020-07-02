package org.goblinframework.transport.protocol;

import org.goblinframework.api.core.GoblinException;

import java.util.Objects;

public class TransportResponseException extends GoblinException {
  private static final long serialVersionUID = -4818779160598751000L;

  private final TransportResponseCode code;

  public TransportResponseException(TransportResponseCode code) {
    this.code = Objects.requireNonNull(code);
  }

  public TransportResponseException(TransportResponseCode code, String message) {
    super(message);
    this.code = Objects.requireNonNull(code);
  }

  public TransportResponseException(TransportResponseCode code, String message, Throwable cause) {
    super(message, cause);
    this.code = Objects.requireNonNull(code);
  }

  public TransportResponseException(TransportResponseCode code, Throwable cause) {
    super(cause);
    this.code = Objects.requireNonNull(code);
  }

  public TransportResponseCode getCode() {
    return code;
  }
}
