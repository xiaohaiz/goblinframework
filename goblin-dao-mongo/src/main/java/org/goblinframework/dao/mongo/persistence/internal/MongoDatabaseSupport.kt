package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.core.annotation.Database
import org.goblinframework.database.mongo.support.MongoClientSupport
import java.util.*

abstract class MongoDatabaseSupport<E, ID> : MongoClientSupport<E, ID>() {

  private val database: String
  private val dynamic: Boolean

  init {
    val annotation = lookupDatabaseAnnotation() ?: throw GoblinDatabaseException("No @Database presented")
    database = annotation.database
    dynamic = annotation.dynamic
  }

  fun getIdDatabaseName(id: ID?): String {
    return if (!dynamic) {
      database
    } else {
      Objects.requireNonNull(id)
      val entity = newEntityInstance()
      setEntityId(entity, id)
      calculateDatabaseName(database, entity)
    }
  }

  fun getEntityDatabaseName(entity: E?): String {
    return if (!dynamic) {
      database
    } else {
      Objects.requireNonNull(entity)
      calculateDatabaseName(database, entity!!)
    }
  }

  abstract fun calculateDatabaseName(template: String, entity: E): String

  private fun lookupDatabaseAnnotation(): Database? {
    val annotation = AnnotationUtils.getAnnotation(javaClass, Database::class.java)
    if (annotation != null) {
      return annotation
    }
    return entityMapping.entityClass.getAnnotation(Database::class.java)
  }

}