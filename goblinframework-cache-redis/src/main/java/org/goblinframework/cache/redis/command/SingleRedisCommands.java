package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.StatefulRedisConnection;
import org.jetbrains.annotations.NotNull;

public class SingleRedisCommands extends RedisCommands {

  private final RedisAsyncCommands async;
  private final RedisSyncCommands sync;

  public SingleRedisCommands(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.async = new SingleRedisAsyncCommands(connection);
    this.sync = new SingleRedisSyncCommands(connection);
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
