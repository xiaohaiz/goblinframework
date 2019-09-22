package org.goblinframework.cache.core.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.cache.GoblinCache
import org.goblinframework.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCacheSystem
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean("CACHE")
class GoblinCacheBuilderImpl(private val delegator: GoblinCacheBuilder)
  : GoblinManagedObject(), GoblinCacheBuilderMXBean, GoblinCacheBuilder {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<GoblinCacheImpl>>()

  override fun getCacheSystem(): GoblinCacheSystem {
    return delegator.cacheSystem
  }

  override fun getCache(name: String): GoblinCache? {
    val id = delegator.decorateCacheName(name)
    var ref = buffer[id]
    ref?.run { return value }
    lock.withLock {
      ref = buffer[id]
      ref?.run { return value }
      val cache = delegator.getCache(id)
      cache?.run {
        val ret = GoblinCacheImpl(this)
        buffer[id] = MutableObject(ret)
        return ret
      } ?: kotlin.run {
        buffer[id] = MutableObject<GoblinCacheImpl>(null)
        return null
      }
    }
  }

  override fun disposeBean() {
    lock.withLock {
      buffer.values.mapNotNull { it.value }.forEach { it.dispose() }
      buffer.clear()
    }
  }
}