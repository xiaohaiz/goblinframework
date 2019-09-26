package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.cache.ICacheBuilderManager
import org.goblinframework.api.core.*
import org.goblinframework.cache.core.GoblinCacheException
import java.util.concurrent.ConcurrentHashMap

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache")
class CacheBuilderManager private constructor()
  : GoblinManagedObject(), ICacheBuilderManager, CacheBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CacheBuilderManager()
  }

  private val buffer = ConcurrentHashMap<CacheSystem, CacheBuilderImpl>()

  @Synchronized
  fun register(builder: CacheBuilder) {
    val system = builder.system()
    buffer[system]?.run {
      throw GoblinCacheException("Cache system $system already exists")
    }
    buffer[system] = CacheBuilderImpl(builder)
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

  override fun getCacheBuilderList(): Array<CacheBuilderMXBean> {
    return buffer.values
        .filter { it.system() !== CacheSystem.NOP }
        .sortedBy { it.system() }
        .toTypedArray()
  }

  @Install
  class Installer : ICacheBuilderManager by INSTANCE
}