package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.core.cache.CacheMXBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@GoblinManagedBean("Cache")
class InJvmCacheBuilder private constructor()
  : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  companion object {
    @JvmField val INSTANCE = InJvmCacheBuilder()
  }

  private val buffer = ConcurrentHashMap<String, InJvmCache>()
  private val lock = ReentrantLock()

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.JVM
  }

  override fun getCache(name: String): Cache {
    buffer[name]?.run { return this }
    lock.withLock {
      buffer[name]?.run { return this }
      val cache = InJvmCache(name)
      buffer[name] = cache
      return cache
    }
  }

  override fun getCacheList(): Array<CacheMXBean> {
    return buffer.values.sortedBy { it.cacheName }.toTypedArray()
  }
}