package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.cache.GoblinCacheException
import org.goblinframework.api.cache.ICacheBuilderManager
import org.goblinframework.api.common.Disposable
import org.goblinframework.api.common.Ordered
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

  private val buffer = ConcurrentHashMap<CacheSystem, CacheBuilder>()

  override fun register(system: CacheSystem, builder: CacheBuilder) {
    buffer.put(system, builder)?.run {
      throw GoblinCacheException("Cache system $system already exists")
    }
  }

  override fun cacheBuilder(system: CacheSystem): CacheBuilder? {
    return buffer[system]
  }

  override fun disposeBean() {
    buffer.values.filterIsInstance(Disposable::class.java).forEach { it.dispose() }
    buffer.clear()
  }

  @Install
  class Installer : Ordered, ICacheBuilderManager by INSTANCE {
    override fun getOrder(): Int {
      return Ordered.HIGHEST_PRECEDENCE
    }
  }
}