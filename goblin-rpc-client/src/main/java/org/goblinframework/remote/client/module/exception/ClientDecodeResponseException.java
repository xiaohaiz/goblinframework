package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteClientException;

public class ClientDecodeResponseException extends RemoteClientException {
  private static final long serialVersionUID = -3720905442192649956L;

  public ClientDecodeResponseException(Throwable cause) {
    super(cause);
  }
}
