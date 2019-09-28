package org.goblinframework.database.mongo.support

import org.goblinframework.database.core.DatabaseSystem
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.core.mapping.EntityMappingBuilder
import org.goblinframework.database.core.mapping.EntityMappingBuilderManager
import org.goblinframework.database.core.support.EntityMappingSupport

abstract class MongoEntityMappingSupport<E, ID> : EntityMappingSupport<E, ID>() {

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    val manager = EntityMappingBuilderManager.INSTANCE
    return manager.getEntityMappingBuilder(DatabaseSystem.MNG)
        ?: throw GoblinDatabaseException("No EntityMappingBuilder of [MNG] registered")
  }
}