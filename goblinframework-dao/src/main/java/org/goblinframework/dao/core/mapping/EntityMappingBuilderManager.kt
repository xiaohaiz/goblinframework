package org.goblinframework.dao.core.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ServiceInstaller
import org.goblinframework.dao.core.module.DatabaseSystem
import java.util.*

@Singleton
class EntityMappingBuilderManager private constructor()
  : GoblinManagedObject(), EntityMappingBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EntityMappingBuilderManager()
  }

  private val buffer = EnumMap<DatabaseSystem, EntityMappingBuilder>(DatabaseSystem::class.java)

  init {
    ServiceInstaller.installedList(EntityMappingBuilderProvider::class.java).forEach {
      val s = it.databaseSystem
      if (buffer[s] != null) {
        throw GoblinDuplicateException()
      }
      buffer[s] = it.entityMappingBuilder
    }
  }

  fun getEntityMappingBuilder(databaseSystem: DatabaseSystem): EntityMappingBuilder? {
    return buffer[databaseSystem]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
  }
}