package org.goblinframework.cache.core.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean(type = "Cache")
class CacheBuilderImpl
internal constructor(private val delegator: CacheBuilder)
  : GoblinManagedObject(), CacheBuilderMXBean, CacheBuilder {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<CacheImpl?>>()

  override fun system(): CacheSystem {
    return delegator.system()
  }

  override fun cache(name: String): Cache? {
    if (system() === CacheSystem.NOP) {
      // Bypass local buffer in case of system is NOP
      return delegator.cache(name)
    }
    buffer[name]?.let { return it.value }
    return lock.withLock {
      buffer[name]?.let { return it.value }
      val cache = delegator.cache(name)?.let { CacheImpl(it) }
      buffer[name] = MutableObject(cache)
      cache
    }
  }

  internal fun asList(): List<CacheImpl> {
    return buffer.values.mapNotNull { it.value }.toList()
  }

  override fun disposeBean() {
    lock.withLock {
      buffer.values.mapNotNull { it.value }.forEach { it.dispose() }
      buffer.clear()
    }
  }

  override fun getCacheSystem(): CacheSystem {
    return system()
  }

  override fun getCacheList(): Array<CacheMXBean> {
    return buffer.values
        .mapNotNull { it.value }
        .sortedWith(object : Comparator<CacheMXBean> {
          override fun compare(o1: CacheMXBean, o2: CacheMXBean): Int {
            val ret = o1.getCacheSystem().compareTo(o2.getCacheSystem())
            if (ret != 0) return ret
            return o1.getCacheName().compareTo(o2.getCacheName())
          }
        })
        .toTypedArray()
  }
}