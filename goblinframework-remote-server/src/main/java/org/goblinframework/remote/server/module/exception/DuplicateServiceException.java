package org.goblinframework.remote.server.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;

public class DuplicateServiceException extends RemoteServerException {
  private static final long serialVersionUID = -3481266184989563805L;

  public DuplicateServiceException(String message) {
    super(message);
  }
}
