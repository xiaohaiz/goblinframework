package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.*;
import org.jetbrains.annotations.NotNull;

public class RedisAsyncCommands {

  private final StatefulRedisConnection<String, Object> connection;

  public RedisAsyncCommands(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.connection = connection;
  }

  public RedisHashAsyncCommands<String, Object> getRedisHashAsyncCommands() {
    return connection.async();
  }

  public RedisKeyAsyncCommands<String, Object> getRedisKeyAsyncCommands() {
    return connection.async();
  }

  public RedisStringAsyncCommands<String, Object> getRedisStringAsyncCommands() {
    return connection.async();
  }

  public RedisListAsyncCommands<String, Object> getRedisListAsyncCommands() {
    return connection.async();
  }

  public RedisSetAsyncCommands<String, Object> getRedisSetAsyncCommands() {
    return connection.async();
  }

  public RedisSortedSetAsyncCommands<String, Object> getRedisSortedSetAsyncCommands() {
    return connection.async();
  }

  public RedisScriptingAsyncCommands<String, Object> getRedisScriptingAsyncCommands() {
    return connection.async();
  }

  public RedisServerAsyncCommands<String, Object> getRedisServerAsyncCommands() {
    return connection.async();
  }

  public RedisHLLAsyncCommands<String, Object> getRedisHLLAsyncCommands() {
    return connection.async();
  }

  public RedisGeoAsyncCommands<String, Object> getRedisGeoAsyncCommands() {
    return connection.async();
  }
}
