package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteClientException;

public class ClientInvocationException extends RemoteClientException {
  private static final long serialVersionUID = -5808301280836388833L;

  public ClientInvocationException(String message) {
    super(message);
  }

  public ClientInvocationException(Throwable cause) {
    super(cause);
  }
}
