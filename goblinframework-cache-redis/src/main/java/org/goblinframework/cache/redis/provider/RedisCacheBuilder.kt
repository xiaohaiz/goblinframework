package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.GoblinCacheBuilder
import org.goblinframework.cache.redis.client.RedisClientManager

@Singleton
class RedisCacheBuilder private constructor() : GoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder()
  }

  override fun getCache(name: String): Cache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
