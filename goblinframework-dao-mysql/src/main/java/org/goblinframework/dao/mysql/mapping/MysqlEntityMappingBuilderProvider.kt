package org.goblinframework.dao.mysql.mapping

import org.goblinframework.api.annotation.Install
import org.goblinframework.dao.core.mapping.EntityMappingBuilder
import org.goblinframework.dao.core.mapping.EntityMappingBuilderProvider
import org.goblinframework.dao.core.module.DatabaseSystem

@Install
class MysqlEntityMappingBuilderProvider : EntityMappingBuilderProvider {

  override fun getDatabaseSystem(): DatabaseSystem {
    return DatabaseSystem.MSQ
  }

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return EntityMappingBuilder(MysqlEntityFieldScanner.INSTANCE, MysqlEntityFieldNameResolver.INSTANCE)
  }
}