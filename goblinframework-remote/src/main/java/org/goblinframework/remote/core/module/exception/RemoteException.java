package org.goblinframework.remote.core.module.exception;

import org.goblinframework.api.core.GoblinException;

abstract public class RemoteException extends GoblinException {
  private static final long serialVersionUID = 1691633674084083629L;

  public RemoteException() {
  }

  public RemoteException(String message) {
    super(message);
  }

  public RemoteException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoteException(Throwable cause) {
    super(cause);
  }
}
