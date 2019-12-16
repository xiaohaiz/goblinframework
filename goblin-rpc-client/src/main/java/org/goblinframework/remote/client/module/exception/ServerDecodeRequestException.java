package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerDecodeRequestException extends RpcServerException {
  private static final long serialVersionUID = -8884483639616755266L;

  public ServerDecodeRequestException(String message) {
    super(message);
  }
}
