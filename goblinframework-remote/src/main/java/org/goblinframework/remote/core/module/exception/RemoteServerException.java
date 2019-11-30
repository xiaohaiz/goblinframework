package org.goblinframework.remote.core.module.exception;

abstract public class RemoteServerException extends RemoteException {
  private static final long serialVersionUID = 3360832569119660369L;

  public RemoteServerException() {
  }

  public RemoteServerException(String message) {
    super(message);
  }

  public RemoteServerException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoteServerException(Throwable cause) {
    super(cause);
  }
}
