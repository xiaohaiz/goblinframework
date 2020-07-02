package org.goblinframework.remote.client.module.exception;

import org.goblinframework.rpc.exception.RpcClientException;

public class ClientUnknownResponseCodeException extends RpcClientException {
  private static final long serialVersionUID = 4790299090178221402L;

  public ClientUnknownResponseCodeException(byte code) {
    super("Unknown remote response code [" + code + "]");
  }
}
