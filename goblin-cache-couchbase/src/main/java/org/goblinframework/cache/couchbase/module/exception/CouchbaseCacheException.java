package org.goblinframework.cache.couchbase.module.exception;

import org.goblinframework.api.core.GoblinException;

public class CouchbaseCacheException extends GoblinException {
  private static final long serialVersionUID = -4077928936442190L;

  public CouchbaseCacheException() {
  }

  public CouchbaseCacheException(String message) {
    super(message);
  }

  public CouchbaseCacheException(String message, Throwable cause) {
    super(message, cause);
  }

  public CouchbaseCacheException(Throwable cause) {
    super(cause);
  }
}
