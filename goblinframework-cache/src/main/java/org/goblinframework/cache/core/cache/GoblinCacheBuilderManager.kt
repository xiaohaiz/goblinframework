package org.goblinframework.cache.core.cache

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.core.annotation.CacheSystem
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

  private val buffer = EnumMap<CacheSystem, GoblinCacheBuilderImpl>(CacheSystem::class.java)

  init {
    ServiceInstaller.installedList(GoblinCacheBuilder::class.java).forEach {
      val system = it.cacheSystem
      buffer[system]?.run {
        throw GoblinDuplicateException("Duplicated GOBLIN cache builder: $system")
      }
      buffer[system] = GoblinCacheBuilderImpl(it)
    }
  }

  fun getCacheBuilder(system: CacheSystem): GoblinCacheBuilder? {
    return buffer[system]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
  }

}