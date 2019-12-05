package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerServiceExecutionException extends RemoteServerException {
  private static final long serialVersionUID = 6598737028257574595L;

  public ServerServiceExecutionException(String message) {
    super(message);
  }
}
