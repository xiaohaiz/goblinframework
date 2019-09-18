package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.core.cache.CacheSystem
import org.goblinframework.cache.core.cache.GoblinCache
import org.goblinframework.cache.core.cache.GoblinCacheBuilder
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfig
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
class RedisCacheBuilder private constructor() : GoblinCacheBuilder {

  companion object {
    val INSTANCE = RedisCacheBuilder()
  }

  private val accessBuffer = ConcurrentHashMap<String, RedisCacheImpl>()
  private val creationLock = ReentrantLock()

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.RDS
  }

  override fun getCache(name: String): GoblinCache? {
    val config = RedisConfigManager.INSTANCE.getRedisConfig(name) ?: return null
    var cache = accessBuffer[config.getName()]
    if (cache == null) {
      creationLock.withLock {
        cache = accessBuffer[config.getName()]
        if (cache == null) {
          cache = createRedisCache(config)
          accessBuffer[config.getName()] = cache!!
        }
      }
    }
    return cache
  }

  override fun destroy() {
    creationLock.withLock {
      accessBuffer.values.forEach { it.destroy() }
      accessBuffer.clear()
    }
  }

  private fun createRedisCache(config: RedisConfig): RedisCacheImpl {
    val client = RedisClientManager.INSTANCE.getRedisClient(config.getName())!!
    return RedisCacheImpl(config.getName(), client)
  }

  @Install
  class Installer : GoblinCacheBuilder by INSTANCE
}
