package org.goblinframework.database.mongo.support

import org.goblinframework.dao.persistence.PersistenceListenerSupport
import org.goblinframework.dao.mongo.mapping.MongoEntityMappingBuilder
import org.goblinframework.dao.mapping.EntityMappingBuilder

abstract class MongoEntityMappingSupport<E, ID> : PersistenceListenerSupport<E, ID>() {

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return MongoEntityMappingBuilder.INSTANCE
  }
}