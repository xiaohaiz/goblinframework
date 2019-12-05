package org.goblinframework.cache.redis.command;

import org.jetbrains.annotations.NotNull;

abstract public class RedisCommands {

  @NotNull
  abstract public RedisAsyncCommands async();

  @NotNull
  abstract public RedisSyncCommands sync();
}
