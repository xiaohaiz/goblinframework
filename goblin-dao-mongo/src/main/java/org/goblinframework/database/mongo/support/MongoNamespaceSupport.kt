package org.goblinframework.database.mongo.support

import com.mongodb.MongoNamespace
import org.goblinframework.dao.mongo.persistence.internal.MongoCollectionSupport
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

  fun getIdNamespace(id: ID?): MongoNamespace {
    val entity = newEntityInstance()
    setEntityId(entity, id)
    return getEntityNamespace(entity)
  }

  fun groupIds(ids: Collection<ID>): LinkedMultiValueMap<MongoNamespace, ID> {
    val ret = LinkedMultiValueMap<MongoNamespace, ID>()
    ids.forEach { ret.add(getIdNamespace(it), it) }
    return ret
  }
}