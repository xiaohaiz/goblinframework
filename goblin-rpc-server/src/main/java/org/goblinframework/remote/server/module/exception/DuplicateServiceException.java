package org.goblinframework.remote.server.module.exception;

import org.goblinframework.rpc.exception.RpcServerException;

public class DuplicateServiceException extends RpcServerException {
  private static final long serialVersionUID = -3481266184989563805L;

  public DuplicateServiceException(String message) {
    super(message);
  }
}
