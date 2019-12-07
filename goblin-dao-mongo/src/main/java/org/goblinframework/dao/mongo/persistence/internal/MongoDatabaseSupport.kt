package org.goblinframework.dao.mongo.persistence.internal

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.dao.core.exception.GoblinDaoException
import org.goblinframework.dao.mongo.annotation.GoblinDatabase
import org.goblinframework.database.mongo.support.MongoClientSupport
import java.util.*

abstract class MongoDatabaseSupport<E, ID> : MongoClientSupport<E, ID>() {

  private val database: String
  private val dynamic: Boolean

  init {
    val annotation = lookupDatabaseAnnotation() ?: throw GoblinDaoException("No @Database presented")
    database = annotation.database
    dynamic = annotation.dynamic
  }

  fun isDynamicDatabase(): Boolean {
    return dynamic
  }

  fun getIdDatabaseName(id: ID?): String {
    return if (!dynamic) {
      database
    } else {
      Objects.requireNonNull(id)
      val entity = newEntityInstance()
      setEntityId(entity, id)
      val database = calculateDatabaseName(database, entity)
      requireNotNull(database)
    }
  }

  fun getEntityDatabaseName(entity: E?): String {
    return if (!dynamic) {
      database
    } else {
      Objects.requireNonNull(entity)
      val database = calculateDatabaseName(database, entity!!)
      requireNotNull(database)
    }
  }

  abstract fun calculateDatabaseName(template: String, entity: E): String?

  private fun lookupDatabaseAnnotation(): GoblinDatabase? {
    return AnnotationUtils.getAnnotation(javaClass, GoblinDatabase::class.java)
  }

}