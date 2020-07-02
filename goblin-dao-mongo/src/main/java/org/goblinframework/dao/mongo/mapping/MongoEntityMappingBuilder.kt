package org.goblinframework.dao.mongo.mapping

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.dao.mapping.EntityMappingBuilder

@Singleton
@GoblinManagedBean("MongoDao")
class MongoEntityMappingBuilder private constructor()
  : EntityMappingBuilder(MongoEntityFieldScanner.INSTANCE, MongoEntityFieldNameResolver.INSTANCE) {

  companion object {
    @JvmField val INSTANCE = MongoEntityMappingBuilder()
  }

}