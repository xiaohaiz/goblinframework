package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerServiceNotFoundException extends RemoteServerException {
  private static final long serialVersionUID = -1026362381771268849L;

  public ServerServiceNotFoundException(String message) {
    super(message);
  }
}
