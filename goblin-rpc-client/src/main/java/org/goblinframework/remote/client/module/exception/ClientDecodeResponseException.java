package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcClientException;

public class ClientDecodeResponseException extends RpcClientException {
  private static final long serialVersionUID = -3720905442192649956L;

  public ClientDecodeResponseException(Throwable cause) {
    super(cause);
  }
}
