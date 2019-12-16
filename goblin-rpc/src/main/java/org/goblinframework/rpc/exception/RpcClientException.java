package org.goblinframework.rpc.exception;

abstract public class RpcClientException extends RpcException {
  private static final long serialVersionUID = -5178654849377469502L;

  public RpcClientException() {
  }

  public RpcClientException(String message) {
    super(message);
  }

  public RpcClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpcClientException(Throwable cause) {
    super(cause);
  }
}
