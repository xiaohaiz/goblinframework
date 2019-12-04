package org.goblinframework.cache.core.module.exception;

import org.goblinframework.api.core.GoblinException;

public class CacheException extends GoblinException {
  private static final long serialVersionUID = -4783276769169355827L;

  public CacheException() {
  }

  public CacheException(String message) {
    super(message);
  }

  public CacheException(String message, Throwable cause) {
    super(message, cause);
  }

  public CacheException(Throwable cause) {
    super(cause);
  }
}
