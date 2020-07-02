package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.dao.mapping.EntityMappingBuilder
import org.goblinframework.dao.mongo.mapping.MongoEntityMappingBuilder
import org.goblinframework.dao.persistence.PersistenceListenerSupport

abstract class MongoPersistenceEntityMappingSupport<E, ID> : PersistenceListenerSupport<E, ID>() {

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return MongoEntityMappingBuilder.INSTANCE
  }
}