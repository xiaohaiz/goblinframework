package org.goblinframework.database.mongo.support

import com.mongodb.MongoNamespace
import org.springframework.util.LinkedMultiValueMap

abstract class MongoNamespaceSupport<E, ID> : MongoCollectionSupport<E, ID>() {

  fun getEntityNamespace(entity: E?): MongoNamespace {
    val database = getEntityDatabaseName(entity)
    val collection = getEntityCollectionName(entity)
    return MongoNamespace(database, collection)
  }

  fun groupEntities(entities: Collection<E>): LinkedMultiValueMap<MongoNamespace, E> {
    val ret = LinkedMultiValueMap<MongoNamespace, E>()
    entities.forEach { ret.add(getEntityNamespace(it), it) }
    return ret
  }
}