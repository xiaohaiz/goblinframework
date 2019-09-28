package org.goblinframework.database.mysql.mapping

import org.goblinframework.database.core.DatabaseSystem
import org.goblinframework.database.core.mapping.EntityMappingBuilder
import org.goblinframework.database.core.mapping.EntityMappingBuilderProvider

class MysqlEntityMappingBuilderProvider private constructor() : EntityMappingBuilderProvider {

  companion object {
    @JvmField val INSTANCE = MysqlEntityMappingBuilderProvider()
  }

  override fun getDatabaseSystem(): DatabaseSystem {
    return DatabaseSystem.MSQ
  }

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return EntityMappingBuilder(MysqlEntityFieldScanner.INSTANCE, MysqlEntityFieldNameResolver.INSTANCE)
  }
}