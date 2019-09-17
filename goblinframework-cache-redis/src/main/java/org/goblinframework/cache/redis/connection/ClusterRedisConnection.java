package org.goblinframework.cache.redis.connection;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.apache.commons.pool2.ObjectPool;
import org.jetbrains.annotations.NotNull;

public class ClusterRedisConnection extends RedisConnection {

  private final StatefulRedisClusterConnection<String, Object> connection;
  private ObjectPool<RedisConnection> connectionPool;

  ClusterRedisConnection(@NotNull StatefulRedisClusterConnection<String, Object> connection) {
    this.connection = connection;
  }

  public void setConnectionPool(@NotNull ObjectPool<RedisConnection> connectionPool) {
    this.connectionPool = connectionPool;
  }

  @Override
  public StatefulRedisClusterConnection<String, Object> getNativeConnection() {
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
