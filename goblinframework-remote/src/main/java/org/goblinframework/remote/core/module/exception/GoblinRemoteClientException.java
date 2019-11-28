package org.goblinframework.remote.core.module.exception;

abstract public class GoblinRemoteClientException extends GoblinRemoteException {
  private static final long serialVersionUID = -5178654849377469502L;

  public GoblinRemoteClientException() {
  }

  public GoblinRemoteClientException(String message) {
    super(message);
  }

  public GoblinRemoteClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinRemoteClientException(Throwable cause) {
    super(cause);
  }
}
