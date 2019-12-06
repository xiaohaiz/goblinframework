package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.dao.mongo.annotation.GoblinCollection
import org.goblinframework.database.core.GoblinDatabaseException
import java.util.*

abstract class MongoCollectionSupport<E, ID> : MongoDatabaseSupport<E, ID>() {

  private val collection: String
  private val dynamic: Boolean

  init {
    val annotation = lookupCollectionAnnotation() ?: throw GoblinDatabaseException("No @Collection presented")
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

  private fun lookupCollectionAnnotation(): GoblinCollection? {
    return AnnotationUtils.getAnnotation(javaClass, GoblinCollection::class.java)
  }
}