package org.goblinframework.cache.redis.command;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.jetbrains.annotations.NotNull;

public class ClusterRedisCommands extends RedisCommands {

  private final RedisAsyncCommands async;
  private final RedisSyncCommands sync;

  public ClusterRedisCommands(@NotNull StatefulRedisClusterConnection<String, Object> connection) {
    this.async = new ClusterRedisAsyncCommands(connection);
    this.sync = new ClusterRedisSyncCommands(connection);
  }

  @NotNull
  @Override
  public RedisAsyncCommands async() {
    return async;
  }

  @NotNull
  @Override
  public RedisSyncCommands sync() {
    return sync;
  }
}
