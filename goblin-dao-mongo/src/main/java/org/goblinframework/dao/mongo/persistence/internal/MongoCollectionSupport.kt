package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.dao.exception.GoblinDaoException
import org.goblinframework.dao.mongo.annotation.MongoPersistenceCollection
import java.util.*

abstract class MongoCollectionSupport<E, ID> : MongoPersistenceDatabaseSupport<E, ID>() {

  private val collection: String
  private val dynamic: Boolean

  init {
    val annotation = lookupCollectionAnnotation() ?: throw GoblinDaoException("No @Collection presented")
    collection = annotation.collection
    dynamic = annotation.dynamic
  }

  fun isDynamicCollection(): Boolean {
    return dynamic
  }

  fun getIdCollectionName(id: ID?): String {
    return if (!dynamic) {
      collection
    } else {
      Objects.requireNonNull(id)
      val entity = newEntityInstance()
      setEntityId(entity, id)
      val collection = calculateCollectionName(collection, entity)
      requireNotNull(collection)
    }
  }

  fun getEntityCollectionName(entity: E?): String {
    return if (!dynamic) {
      collection
    } else {
      Objects.requireNonNull(entity)
      val collection = calculateCollectionName(collection, entity!!)
      requireNotNull(collection)
    }
  }

  abstract fun calculateCollectionName(template: String, entity: E): String?

  private fun lookupCollectionAnnotation(): MongoPersistenceCollection? {
    return AnnotationUtils.getAnnotation(javaClass, MongoPersistenceCollection::class.java)
  }
}