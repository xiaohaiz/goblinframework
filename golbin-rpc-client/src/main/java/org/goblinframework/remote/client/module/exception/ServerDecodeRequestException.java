package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerDecodeRequestException extends RemoteServerException {
  private static final long serialVersionUID = -8884483639616755266L;

  public ServerDecodeRequestException(String message) {
    super(message);
  }
}
