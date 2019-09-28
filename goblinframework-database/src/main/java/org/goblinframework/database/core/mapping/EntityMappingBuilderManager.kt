package org.goblinframework.database.core.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.core.GoblinDatabaseSystem
import java.util.concurrent.ConcurrentHashMap

@Singleton
class EntityMappingBuilderManager private constructor()
  : GoblinManagedObject(), EntityMappingBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EntityMappingBuilderManager()
  }

  private val buffer = ConcurrentHashMap<GoblinDatabaseSystem, EntityMappingBuilder>()

  fun registerEntityMappingBuilderProvider(provider: EntityMappingBuilderProvider) {
    buffer.put(provider.databaseSystem, provider.entityMappingBuilder)?.run {
      throw GoblinDatabaseException("EntityMappingBuilderProvider [${provider.databaseSystem}] already exists")
    }
  }

  fun getEntityMappingBuilder(databaseSystem: GoblinDatabaseSystem): EntityMappingBuilder? {
    return buffer[databaseSystem]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
    buffer.clear()
  }
}