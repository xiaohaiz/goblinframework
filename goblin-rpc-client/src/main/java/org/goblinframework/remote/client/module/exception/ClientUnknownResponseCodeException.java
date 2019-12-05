package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteClientException;

public class ClientUnknownResponseCodeException extends RemoteClientException {
  private static final long serialVersionUID = 4790299090178221402L;

  public ClientUnknownResponseCodeException(byte code) {
    super("Unknown remote response code [" + code + "]");
  }
}
