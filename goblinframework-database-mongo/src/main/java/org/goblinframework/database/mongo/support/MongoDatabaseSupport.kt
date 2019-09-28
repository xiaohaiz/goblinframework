package org.goblinframework.database.mongo.support

import org.goblinframework.api.database.Database
import org.goblinframework.database.core.GoblinDatabaseException
import java.util.*

abstract class MongoDatabaseSupport<E, ID> : MongoClientSupport<E, ID>() {

  private val database: String
  private val dynamic: Boolean

  init {
    val entityClass = entityMapping.entityClass
    val annotation = entityClass.getAnnotation(Database::class.java)
        ?: throw GoblinDatabaseException("No @Database presented on ${entityClass.name}")
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

}