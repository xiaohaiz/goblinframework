package org.goblinframework.dao.mongo.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.database.core.mapping.EntityMappingBuilder
import org.goblinframework.database.mongo.mapping.MongoEntityFieldNameResolver
import org.goblinframework.database.mongo.mapping.MongoEntityFieldScanner

@Singleton
@GoblinManagedBean("MongoDao")
class MongoEntityMappingBuilder private constructor()
  : EntityMappingBuilder(MongoEntityFieldScanner.INSTANCE, MongoEntityFieldNameResolver.INSTANCE) {

  companion object {
    @JvmField val INSTANCE = MongoEntityMappingBuilder()
  }

}