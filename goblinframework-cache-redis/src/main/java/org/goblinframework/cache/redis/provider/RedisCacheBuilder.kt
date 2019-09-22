package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.core.cache.GoblinCache
import org.goblinframework.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCacheSystem

@Install
class RedisCacheBuilder : GoblinCacheBuilder {

  override fun getCacheSystem(): GoblinCacheSystem {
    return GoblinCacheSystem.RDS
  }

  override fun getCache(name: String): GoblinCache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
