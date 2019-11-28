package org.goblinframework.remote.core.module.exception;

abstract public class GoblinRemoteServiceException extends GoblinRemoteException {
  private static final long serialVersionUID = 3360832569119660369L;

  public GoblinRemoteServiceException() {
  }

  public GoblinRemoteServiceException(String message) {
    super(message);
  }

  public GoblinRemoteServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinRemoteServiceException(Throwable cause) {
    super(cause);
  }
}
