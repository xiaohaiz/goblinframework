package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.module.exception.CacheException
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache")
class CacheBuilderManager private constructor()
  : GoblinManagedObject(), CacheBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CacheBuilderManager()
  }

  private val buffer = ConcurrentHashMap<CacheSystem, CacheBuilderImpl>()

  @Synchronized
  fun register(builder: CacheBuilder) {
    val system = builder.getCacheSystem()
    buffer[system]?.run {
      throw CacheException("Cache system $system already exists")
    }
    buffer[system] = CacheBuilderImpl(builder)
  }

  fun cacheBuilder(system: CacheSystem): CacheBuilder? {
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
        .filter { it.getCacheSystem() !== CacheSystem.NOP }
        .sortedBy { it.getCacheSystem() }
        .toTypedArray()
  }

}