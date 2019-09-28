package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.cache.redis.client.RedisClientManager

@Singleton
class RedisCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder()
  }

  override fun system(): CacheSystem {
    return CacheSystem.RDS
  }

  override fun cache(name: String): Cache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
