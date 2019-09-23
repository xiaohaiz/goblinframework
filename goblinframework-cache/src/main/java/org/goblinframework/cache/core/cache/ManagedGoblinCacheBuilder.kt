package org.goblinframework.cache.core.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.GoblinCacheBuilder
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean(type = "cache", name = "GoblinCacheBuilder")
class ManagedGoblinCacheBuilder
internal constructor(private val delegator: GoblinCacheBuilder)
  : GoblinManagedObject(), GoblinCacheBuilderMXBean, GoblinCacheBuilder {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<CacheImpl>>()

  override fun getCache(name: String): Cache? {
    val id = delegator.decorateCacheName(name)
    var ref = buffer[id]
    ref?.run { return value }
    lock.withLock {
      ref = buffer[id]
      ref?.run { return value }
      val cache = delegator.getCache(id)
      cache?.run {
        val ret = CacheImpl(this)
        buffer[id] = MutableObject(ret)
        return ret
      } ?: kotlin.run {
        buffer[id] = MutableObject<CacheImpl>(null)
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