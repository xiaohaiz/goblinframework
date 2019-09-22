package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.core.cache.GoblinCache
import org.goblinframework.core.cache.GoblinCacheBuilder

@Singleton
class RedisCacheBuilder private constructor() : GoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder()
  }

  override fun getCache(name: String): GoblinCache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
