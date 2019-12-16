package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerServiceNotFoundException extends RpcServerException {
  private static final long serialVersionUID = -1026362381771268849L;

  public ServerServiceNotFoundException(String message) {
    super(message);
  }
}
