package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.*;
import org.jetbrains.annotations.NotNull;

public class RedisSyncCommands {

  private final StatefulRedisConnection<String, Object> connection;

  public RedisSyncCommands(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.connection = connection;
  }

  public RedisHashCommands<String, Object> getRedisHashCommands() {
    return connection.sync();
  }

  public RedisKeyCommands<String, Object> getRedisKeyCommands() {
    return connection.sync();
  }

  public RedisStringCommands<String, Object> getRedisStringCommands() {
    return connection.sync();
  }

  public RedisListCommands<String, Object> getRedisListCommands() {
    return connection.sync();
  }

  public RedisSetCommands<String, Object> getRedisSetCommands() {
    return connection.sync();
  }

  public RedisSortedSetCommands<String, Object> getRedisSortedSetCommands() {
    return connection.sync();
  }

  public RedisScriptingCommands<String, Object> getRedisScriptingCommands() {
    return connection.sync();
  }

  public RedisServerCommands<String, Object> getRedisServerCommands() {
    return connection.sync();
  }

  public RedisHLLCommands<String, Object> getRedisHLLCommands() {
    return connection.sync();
  }

  public RedisGeoCommands<String, Object> getRedisGeoCommands() {
    return connection.sync();
  }
}
