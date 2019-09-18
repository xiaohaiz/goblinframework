package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ServiceInstaller
import java.util.*

@Singleton
class GoblinCacheBuilderManager private constructor()
  : GoblinManagedObject(), GoblinCacheBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = GoblinCacheBuilderManager()
  }

  private val buffer = EnumMap<CacheSystem, GoblinCacheBuilder>(CacheSystem::class.java)

  init {
    ServiceInstaller.installedList(GoblinCacheBuilder::class.java).forEach {
      val previous = buffer.put(it.cacheSystem, it)
      previous?.run {
        throw GoblinDuplicateException("Cache builder [${it.cacheSystem}] duplicated")
      }
    }
  }

  fun getCacheBuilder(system: CacheSystem): GoblinCacheBuilder? {
    return buffer[system]
  }

  fun destroy() {
    unregisterIfNecessary()
    buffer.values.forEach { it.destroy() }
  }
}