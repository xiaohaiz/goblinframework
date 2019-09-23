package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.cache.GoblinCacheBuilder
import org.goblinframework.api.cache.GoblinCacheSystem
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.module.spi.RegisterGoblinCacheBuilder
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "cache")
class GoblinCacheBuilderManager private constructor()
  : GoblinManagedObject(), GoblinCacheBuilderManagerMXBean, RegisterGoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = GoblinCacheBuilderManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = EnumMap<GoblinCacheSystem, ManagedGoblinCacheBuilder>(GoblinCacheSystem::class.java)

  fun getCacheBuilder(system: GoblinCacheSystem): GoblinCacheBuilder? {
    return lock.read { buffer[system] }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }

  override fun register(system: GoblinCacheSystem, builder: GoblinCacheBuilder) {
    lock.write {
      buffer[system]?.run { throw GoblinDuplicateException() }
      buffer[system] = ManagedGoblinCacheBuilder(builder)
    }
  }

  @Install
  class Installer : RegisterGoblinCacheBuilder by INSTANCE
}