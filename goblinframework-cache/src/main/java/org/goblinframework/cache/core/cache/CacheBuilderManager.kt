package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.*
import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache")
class CacheBuilderManager private constructor()
  : GoblinManagedObject(), ICacheBuilderManager, CacheBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CacheBuilderManager()
  }

  private val buffer = ConcurrentHashMap<CacheSystem, ManagedCacheBuilder>()

  @Synchronized
  fun register(system: CacheSystem, builder: CacheBuilder) {
    buffer[system]?.run {
      throw GoblinCacheException("Cache system $system already exists")
    }
    buffer[system] = ManagedCacheBuilder(builder)
  }

  override fun cacheBuilder(system: CacheSystem): CacheBuilder? {
    return buffer[system]
  }

  fun asCacheList(system: CacheSystem): List<Cache> {
    return buffer[system]?.asList() ?: emptyList()
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
    buffer.clear()
  }

  @Install
  class Installer : ICacheBuilderManager by INSTANCE
}