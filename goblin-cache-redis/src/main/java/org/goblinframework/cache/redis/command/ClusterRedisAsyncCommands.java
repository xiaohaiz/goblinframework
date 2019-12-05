package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.async.*;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.jetbrains.annotations.NotNull;

public class ClusterRedisAsyncCommands extends RedisAsyncCommands {

  private final StatefulRedisClusterConnection<String, Object> connection;

  public ClusterRedisAsyncCommands(@NotNull StatefulRedisClusterConnection<String, Object> connection) {
    this.connection = connection;
  }

  @Override
  public RedisHashAsyncCommands<String, Object> getRedisHashAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisKeyAsyncCommands<String, Object> getRedisKeyAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisStringAsyncCommands<String, Object> getRedisStringAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisListAsyncCommands<String, Object> getRedisListAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisSetAsyncCommands<String, Object> getRedisSetAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisSortedSetAsyncCommands<String, Object> getRedisSortedSetAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisScriptingAsyncCommands<String, Object> getRedisScriptingAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisServerAsyncCommands<String, Object> getRedisServerAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisHLLAsyncCommands<String, Object> getRedisHLLAsyncCommands() {
    return connection.async();
  }

  @Override
  public RedisGeoAsyncCommands<String, Object> getRedisGeoAsyncCommands() {
    return connection.async();
  }
}
