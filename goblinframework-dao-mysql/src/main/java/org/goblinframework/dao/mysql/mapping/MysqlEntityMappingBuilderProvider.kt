package org.goblinframework.dao.mysql.mapping

import org.goblinframework.dao.core.GoblinDatabaseSystem
import org.goblinframework.dao.core.mapping.EntityMappingBuilder
import org.goblinframework.dao.core.mapping.EntityMappingBuilderProvider

class MysqlEntityMappingBuilderProvider private constructor() : EntityMappingBuilderProvider {

  companion object {
    @JvmField val INSTANCE = MysqlEntityMappingBuilderProvider()
  }

  override fun getDatabaseSystem(): GoblinDatabaseSystem {
    return GoblinDatabaseSystem.MSQ
  }

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return EntityMappingBuilder(MysqlEntityFieldScanner.INSTANCE, MysqlEntityFieldNameResolver.INSTANCE)
  }
}