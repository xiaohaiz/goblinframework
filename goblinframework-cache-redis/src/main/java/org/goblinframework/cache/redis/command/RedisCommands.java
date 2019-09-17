package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.StatefulRedisConnection;
import org.jetbrains.annotations.NotNull;

public class RedisCommands {

  private final RedisAsyncCommands async;
  private final RedisSyncCommands sync;

  public RedisCommands(@NotNull StatefulRedisConnection<String, Object> connection) {
    this.async = new RedisAsyncCommands(connection);
    this.sync = new RedisSyncCommands(connection);
  }

  @NotNull
  public RedisAsyncCommands async() {
    return async;
  }

  @NotNull
  public RedisSyncCommands sync() {
    return sync;
  }
}
