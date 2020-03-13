package org.goblinframework.cache.redis.connection;

abstract public class RedisConnection implements AutoCloseable {

  abstract public Object getNativeConnection();

  @Override
  public void close() {
    doClose();
  }

  abstract protected void doClose();

  abstract public void closeNativeConnection();

}
