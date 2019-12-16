package org.goblinframework.rpc.exception;

abstract public class RpcServerException extends RpcException {
  private static final long serialVersionUID = 3360832569119660369L;

  public RpcServerException() {
  }

  public RpcServerException(String message) {
    super(message);
  }

  public RpcServerException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpcServerException(Throwable cause) {
    super(cause);
  }
}
