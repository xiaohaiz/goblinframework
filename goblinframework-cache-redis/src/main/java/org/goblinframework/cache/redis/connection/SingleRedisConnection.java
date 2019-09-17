package org.goblinframework.cache.redis.connection;

import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.pool2.ObjectPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleRedisConnection extends RedisConnection {

  private final StatefulRedisConnection<String, Object> connection;
  private ObjectPool<RedisConnection> connectionPool;

  public SingleRedisConnection(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.connection = connection;
  }

  public void setConnectionPool(@Nullable ObjectPool<RedisConnection> connectionPool) {
    this.connectionPool = connectionPool;
  }

  @Override
  public StatefulRedisConnection<String, Object> getNativeConnection() {
    return connection;
  }

  @Override
  protected void doClose() {
    if (connectionPool != null) {
      try {
        connectionPool.returnObject(this);
      } catch (Exception ignore) {
      }
    }
  }
}
