package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcClientException;

public class ClientInvocationException extends RpcClientException {
  private static final long serialVersionUID = -5808301280836388833L;

  public ClientInvocationException(String message) {
    super(message);
  }

  public ClientInvocationException(Throwable cause) {
    super(cause);
  }
}
