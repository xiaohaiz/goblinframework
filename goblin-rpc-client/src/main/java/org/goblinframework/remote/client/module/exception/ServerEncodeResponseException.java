package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class ServerEncodeResponseException extends RpcServerException {
  private static final long serialVersionUID = -6742129734030299518L;

  public ServerEncodeResponseException(String message) {
    super(message);
  }
}
