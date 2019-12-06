package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.api.dao.Collection
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.mongo.support.MongoDatabaseSupport
import java.util.*

abstract class MongoCollectionSupport<E, ID> : MongoDatabaseSupport<E, ID>() {

  private val collection: String
  private val dynamic: Boolean

  init {
    val entityClass = entityMapping.entityClass
    val annotation = entityClass.getAnnotation(Collection::class.java)
        ?: throw GoblinDatabaseException("No @Collection presented on ${entityClass.name}")
    collection = annotation.collection
    dynamic = annotation.dynamic
  }

  fun getIdCollectionName(id: ID?): String {
    return if (!dynamic) {
      collection
    } else {
      Objects.requireNonNull(id)
      val entity = newEntityInstance()
      setEntityId(entity, id)
      calculateCollectionName(collection, entity)
    }
  }

  fun getEntityCollectionName(entity: E?): String {
    return if (!dynamic) {
      collection
    } else {
      Objects.requireNonNull(entity)
      calculateCollectionName(collection, entity!!)
    }
  }

  abstract fun calculateCollectionName(template: String, entity: E): String

}