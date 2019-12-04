package org.goblinframework.remote.core.module.exception;

abstract public class RemoteClientException extends RemoteException {
  private static final long serialVersionUID = -5178654849377469502L;

  public RemoteClientException() {
  }

  public RemoteClientException(String message) {
    super(message);
  }

  public RemoteClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoteClientException(Throwable cause) {
    super(cause);
  }
}
