package org.goblinframework.remote.server.module.exception;

import org.goblinframework.remote.core.module.exception.RemoteServerException;
import org.jetbrains.annotations.NotNull;

public class ServiceCreationException extends RemoteServerException {
  private static final long serialVersionUID = 6009723189531729585L;

  public ServiceCreationException(Throwable cause) {
    super(cause);
  }

  public static void rethrow(@NotNull Throwable error) {
    if (error instanceof ServiceCreationException) {
      throw (ServiceCreationException) error;
    } else {
      throw new ServiceCreationException(error);
    }
  }
}
