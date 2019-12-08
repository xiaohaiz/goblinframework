package org.goblinframework.dao.mysql.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.dao.mapping.EntityMappingBuilder
import org.goblinframework.database.mysql.mapping.MysqlEntityFieldNameResolver
import org.goblinframework.database.mysql.mapping.MysqlEntityFieldScanner

@Singleton
@GoblinManagedBean("MysqlDao")
class MysqlEntityMappingBuilder private constructor()
  : EntityMappingBuilder(MysqlEntityFieldScanner.INSTANCE, MysqlEntityFieldNameResolver.INSTANCE) {

  companion object {
    @JvmField val INSTANCE = MysqlEntityMappingBuilder()
  }
}