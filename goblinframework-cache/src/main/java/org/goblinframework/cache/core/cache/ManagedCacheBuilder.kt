package org.goblinframework.cache.core.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean(type = "cache")
class ManagedCacheBuilder
internal constructor(private val delegator: CacheBuilder)
  : GoblinManagedObject(), CacheBuilderMXBean, CacheBuilder {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<ManagedCache?>>()

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
      val cache = delegator.cache(name)?.let { ManagedCache(it) }
      buffer[name] = MutableObject(cache)
      cache
    }
  }

  internal fun asList(): List<ManagedCache> {
    return buffer.values.mapNotNull { it.value }.toList()
  }

  override fun disposeBean() {
    lock.withLock {
      buffer.values.mapNotNull { it.value }.forEach { it.dispose() }
      buffer.clear()
    }
  }
}