package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerBackPressureException extends RemoteServerException {
  private static final long serialVersionUID = -1033526354769729870L;

  public ServerBackPressureException(String message) {
    super(message);
  }
}
