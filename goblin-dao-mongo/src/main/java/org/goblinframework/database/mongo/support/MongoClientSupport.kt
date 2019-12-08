package org.goblinframework.database.mongo.support

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.dao.annotation.PersistenceConnection
import org.goblinframework.dao.exception.GoblinDaoException
import org.goblinframework.database.mongo.client.MongoClient
import org.goblinframework.database.mongo.client.MongoClientManager

abstract class MongoClientSupport<E, ID> : MongoEntityMappingSupport<E, ID>() {

  private val client: MongoClient

  init {
    val clazz = ClassUtils.filterCglibProxyClass(javaClass)
    val annotation = AnnotationUtils.getAnnotation(clazz, PersistenceConnection::class.java)
        ?: throw GoblinDaoException("No @PersistenceConnection presented on ${clazz.name}")
    val connection = annotation.connection
    client = MongoClientManager.INSTANCE.getMongoClient(connection)
        ?: throw GoblinDaoException("Mongo client [$connection] not found")
  }

  fun getMongoClient(): MongoClient {
    return client
  }

  fun getNativeMongoClient(): com.mongodb.reactivestreams.client.MongoClient {
    return client.getNativeClient()
  }
}