package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerMethodNotFoundException extends RpcServerException {
  private static final long serialVersionUID = 6299786083174958916L;

  public ServerMethodNotFoundException(String message) {
    super(message);
  }
}
