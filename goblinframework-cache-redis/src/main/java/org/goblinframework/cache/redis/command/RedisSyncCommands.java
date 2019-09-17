package org.goblinframework.cache.redis.command;

import io.lettuce.core.api.sync.*;

abstract public class RedisSyncCommands {

  abstract public RedisHashCommands<String, Object> getRedisHashCommands();

  abstract public RedisKeyCommands<String, Object> getRedisKeyCommands();

  abstract public RedisStringCommands<String, Object> getRedisStringCommands();

  abstract public RedisListCommands<String, Object> getRedisListCommands();

  abstract public RedisSetCommands<String, Object> getRedisSetCommands();

  abstract public RedisSortedSetCommands<String, Object> getRedisSortedSetCommands();

  abstract public RedisScriptingCommands<String, Object> getRedisScriptingCommands();

  abstract public RedisServerCommands<String, Object> getRedisServerCommands();

  abstract public RedisHLLCommands<String, Object> getRedisHLLCommands();

  abstract public RedisGeoCommands<String, Object> getRedisGeoCommands();
}
