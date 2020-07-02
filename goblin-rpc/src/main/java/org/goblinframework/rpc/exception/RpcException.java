package org.goblinframework.rpc.exception;

import org.goblinframework.api.core.GoblinException;

abstract public class RpcException extends GoblinException {
  private static final long serialVersionUID = 1691633674084083629L;

  public RpcException() {
  }

  public RpcException(String message) {
    super(message);
  }

  public RpcException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpcException(Throwable cause) {
    super(cause);
  }
}
