package org.goblinframework.database.mongo.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.database.core.DatabaseSystem
import org.goblinframework.database.core.mapping.EntityMappingBuilder
import org.goblinframework.database.core.mapping.EntityMappingBuilderProvider

@Singleton
class MongoEntityMappingBuilderProvider private constructor() : EntityMappingBuilderProvider {

  companion object {
    @JvmField val INSTANCE = MongoEntityMappingBuilderProvider()
  }

  override fun getDatabaseSystem(): DatabaseSystem {
    return DatabaseSystem.MNG
  }

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return EntityMappingBuilder(MongoEntityFieldScanner.INSTANCE, MongoEntityFieldNameResolver.INSTANCE)
  }
}