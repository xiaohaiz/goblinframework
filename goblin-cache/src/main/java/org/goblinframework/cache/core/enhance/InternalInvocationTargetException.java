package org.goblinframework.cache.core.enhance;

import org.goblinframework.cache.core.module.exception.CacheException;

public class InternalInvocationTargetException extends CacheException {
  private static final long serialVersionUID = 7820802986696337666L;

  public InternalInvocationTargetException(Throwable cause) {
    super(cause);
  }
}
