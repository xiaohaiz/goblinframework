package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerServiceExecutionException extends RpcServerException {
  private static final long serialVersionUID = 6598737028257574595L;

  public ServerServiceExecutionException(String message) {
    super(message);
  }
}
