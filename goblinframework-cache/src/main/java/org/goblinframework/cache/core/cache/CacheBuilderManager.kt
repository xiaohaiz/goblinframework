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
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache")
class CacheBuilderManager private constructor()
  : GoblinManagedObject(), ICacheBuilderManager, CacheBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CacheBuilderManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = EnumMap<CacheSystem, CacheBuilder>(CacheSystem::class.java)

  override fun register(system: CacheSystem, builder: CacheBuilder) {
    lock.write {
      buffer[system]?.run {
        throw GoblinCacheException("Cache system $system already exists")
      }
      if (builder is Disposable) {
        buffer[system] = builder
      } else {
        buffer[system] = ManagedCacheBuilder(builder)
      }
    }
  }

  override fun cacheBuilder(system: CacheSystem): CacheBuilder? {
    return lock.read { buffer[system] }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.filterIsInstance(Disposable::class.java).forEach { it.dispose() }
      buffer.clear()
    }
  }

  @Install
  class Installer : Ordered, ICacheBuilderManager by INSTANCE {
    override fun getOrder(): Int {
      return Ordered.HIGHEST_PRECEDENCE
    }
  }
}