package org.goblinframework.remote.client.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class ServerMethodNotFoundException extends RemoteServerException {
  private static final long serialVersionUID = 6299786083174958916L;

  public ServerMethodNotFoundException(String message) {
    super(message);
  }
}
