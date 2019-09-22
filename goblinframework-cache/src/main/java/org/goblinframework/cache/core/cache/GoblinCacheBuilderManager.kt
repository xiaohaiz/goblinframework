package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCacheSystem
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.module.spi.RegisterGoblinCacheBuilder
import org.goblinframework.core.util.ServiceInstaller
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("cache")
class GoblinCacheBuilderManager private constructor()
  : GoblinManagedObject(), GoblinCacheBuilderManagerMXBean, RegisterGoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = GoblinCacheBuilderManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = EnumMap<GoblinCacheSystem, GoblinCacheBuilderImpl>(GoblinCacheSystem::class.java)

  init {
    ServiceInstaller.installedList(GoblinCacheBuilder::class.java).forEach {
      val system = it.cacheSystem
      buffer[system]?.run {
        throw GoblinDuplicateException("Duplicated GOBLIN cache builder: $system")
      }
      buffer[system] = GoblinCacheBuilderImpl(it)
    }
  }

  fun getCacheBuilder(system: GoblinCacheSystem): GoblinCacheBuilder? {
    return buffer[system]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
  }

  override fun register(system: GoblinCacheSystem, builder: GoblinCacheBuilder) {
    lock.write {
      buffer[system]?.run { throw GoblinDuplicateException() }
    }
  }

  @Install
  class Installer : RegisterGoblinCacheBuilder by INSTANCE
}