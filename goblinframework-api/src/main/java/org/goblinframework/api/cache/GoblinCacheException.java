package org.goblinframework.api.cache;

import org.goblinframework.api.common.GoblinException;

public class GoblinCacheException extends GoblinException {
  private static final long serialVersionUID = -4783276769169355827L;

  public GoblinCacheException() {
  }

  public GoblinCacheException(String message) {
    super(message);
  }

  public GoblinCacheException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinCacheException(Throwable cause) {
    super(cause);
  }
}
