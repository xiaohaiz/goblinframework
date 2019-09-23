package org.goblinframework.cache.redis.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.redis.client.RedisClientManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache.redis")
class RedisCacheBuilder private constructor()
  : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder()
  }

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, RedisCacheImpl>()

  override fun cache(name: String): Cache? {
    val client = RedisClientManager.INSTANCE.getRedisClient(name) ?: return null
    return RedisCacheImpl(name, client)
  }
}
