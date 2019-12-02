package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerEncodeResponseException extends RemoteServerException {
  private static final long serialVersionUID = -6742129734030299518L;

  public ServerEncodeResponseException(String message) {
    super(message);
  }
}
