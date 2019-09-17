package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.*;
import org.jetbrains.annotations.NotNull;

public class SingleRedisSyncCommands extends RedisSyncCommands {

  private final StatefulRedisConnection<String, Object> connection;

  public SingleRedisSyncCommands(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.connection = connection;
  }

  @Override
  public RedisHashCommands<String, Object> getRedisHashCommands() {
    return connection.sync();
  }

  @Override
  public RedisKeyCommands<String, Object> getRedisKeyCommands() {
    return connection.sync();
  }

  @Override
  public RedisStringCommands<String, Object> getRedisStringCommands() {
    return connection.sync();
  }

  @Override
  public RedisListCommands<String, Object> getRedisListCommands() {
    return connection.sync();
  }

  @Override
  public RedisSetCommands<String, Object> getRedisSetCommands() {
    return connection.sync();
  }

  @Override
  public RedisSortedSetCommands<String, Object> getRedisSortedSetCommands() {
    return connection.sync();
  }

  @Override
  public RedisScriptingCommands<String, Object> getRedisScriptingCommands() {
    return connection.sync();
  }

  @Override
  public RedisServerCommands<String, Object> getRedisServerCommands() {
    return connection.sync();
  }

  @Override
  public RedisHLLCommands<String, Object> getRedisHLLCommands() {
    return connection.sync();
  }

  @Override
  public RedisGeoCommands<String, Object> getRedisGeoCommands() {
    return connection.sync();
  }
}
