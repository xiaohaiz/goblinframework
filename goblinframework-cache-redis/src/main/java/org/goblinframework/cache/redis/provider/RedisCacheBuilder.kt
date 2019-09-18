package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.CacheSystem
import org.goblinframework.cache.core.cache.GoblinCache
import org.goblinframework.cache.core.cache.GoblinCacheBuilder
import org.goblinframework.cache.redis.client.RedisClientManager

@Install
class RedisCacheBuilder : GoblinCacheBuilder {

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.RDS
  }

  override fun getCache(name: String): GoblinCache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
