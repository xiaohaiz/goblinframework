package org.goblinframework.cache.redis.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.core.cache.CacheMXBean
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheRedis")
class RedisCacheBuilder2 private constructor()
  : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder2()
  }

  private val buffer = ConcurrentHashMap<String, MutableObject<RedisCache?>>()
  private val lock = ReentrantLock()

  override fun initializeBean() {
    CacheBuilderManager.INSTANCE.registerCacheBuilder(this)
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.RDS
  }

  override fun getCache(name: String): Cache? {
    buffer[name]?.run { return this.value }
    lock.withLock {
      buffer[name]?.run { return this.value }
      val mutable = MutableObject<RedisCache?>()
      RedisClientManager.INSTANCE.getRedisClient(name)?.run {
        mutable.value = RedisCache(this)
      }
      buffer[name] = mutable
      return mutable.value
    }
  }

  override fun getCacheList(): Array<CacheMXBean> {
    return buffer.values.mapNotNull { it.value }.sortedBy { it.cacheName }.toTypedArray()
  }

  override fun disposeBean() {
    CacheBuilderManager.INSTANCE.unregisterCacheBuilder(this)
  }
}