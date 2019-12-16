package org.goblinframework.rpc.exception;

public class RpcConfigException extends RpcException {
  private static final long serialVersionUID = -492294524342775387L;

  public RpcConfigException() {
  }

  public RpcConfigException(String message) {
    super(message);
  }

  public RpcConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpcConfigException(Throwable cause) {
    super(cause);
  }
}
