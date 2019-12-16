package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerBackPressureException extends RpcServerException {
  private static final long serialVersionUID = -1033526354769729870L;

  public ServerBackPressureException(String message) {
    super(message);
  }
}
