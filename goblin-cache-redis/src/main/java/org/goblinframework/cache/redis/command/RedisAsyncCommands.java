package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.async.*;

abstract public class RedisAsyncCommands {

  abstract public RedisHashAsyncCommands<String, Object> getRedisHashAsyncCommands();

  abstract public RedisKeyAsyncCommands<String, Object> getRedisKeyAsyncCommands();

  abstract public RedisStringAsyncCommands<String, Object> getRedisStringAsyncCommands();

  abstract public RedisListAsyncCommands<String, Object> getRedisListAsyncCommands();

  abstract public RedisSetAsyncCommands<String, Object> getRedisSetAsyncCommands();

  abstract public RedisSortedSetAsyncCommands<String, Object> getRedisSortedSetAsyncCommands();

  abstract public RedisScriptingAsyncCommands<String, Object> getRedisScriptingAsyncCommands();

  abstract public RedisServerAsyncCommands<String, Object> getRedisServerAsyncCommands();

  abstract public RedisHLLAsyncCommands<String, Object> getRedisHLLAsyncCommands();

  abstract public RedisGeoAsyncCommands<String, Object> getRedisGeoAsyncCommands();
}
