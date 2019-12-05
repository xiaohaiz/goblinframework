package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteClientException;

public class ClientEncodeRequestException extends RemoteClientException {
  private static final long serialVersionUID = -7485090089589811142L;

  public ClientEncodeRequestException(Throwable cause) {
    super(cause);
  }
}
