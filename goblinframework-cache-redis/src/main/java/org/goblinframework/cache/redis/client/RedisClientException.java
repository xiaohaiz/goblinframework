package org.goblinframework.cache.redis.client;

import org.goblinframework.api.core.GoblinException;

public class RedisClientException extends GoblinException {
  private static final long serialVersionUID = -2210367757631846617L;

  public RedisClientException() {
  }

  public RedisClientException(String message) {
    super(message);
  }

  public RedisClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public RedisClientException(Throwable cause) {
    super(cause);
  }
}
