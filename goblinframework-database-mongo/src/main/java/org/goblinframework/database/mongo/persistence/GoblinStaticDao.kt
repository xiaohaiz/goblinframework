package org.goblinframework.database.mongo.persistence

import org.goblinframework.database.mongo.support.MongoDatabaseSupport

abstract class GoblinStaticDao<E, ID> : MongoDatabaseSupport<E, ID>() {

  override fun calculateDatabaseName(template: String, entity: E): String {
    throw UnsupportedOperationException()
  }

  fun getDatabaseName(): String {
    return getEntityDatabaseName(null)
  }
}