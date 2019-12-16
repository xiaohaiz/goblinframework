package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcClientException;

public class ClientEncodeRequestException extends RpcClientException {
  private static final long serialVersionUID = -7485090089589811142L;

  public ClientEncodeRequestException(Throwable cause) {
    super(cause);
  }
}
