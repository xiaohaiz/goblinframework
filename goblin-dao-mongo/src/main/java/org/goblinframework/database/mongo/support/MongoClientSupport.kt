package org.goblinframework.database.mongo.support

import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.database.core.GoblinDatabaseConnection
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.mongo.client.MongoClient
import org.goblinframework.database.mongo.client.MongoClientManager

abstract class MongoClientSupport<E, ID> : MongoEntityMappingSupport<E, ID>() {

  private val client: MongoClient

  init {
    val clazz = ClassUtils.filterCglibProxyClass(javaClass)
    val annotation = AnnotationUtils.getAnnotation(clazz, GoblinDatabaseConnection::class.java)
        ?: throw GoblinDatabaseException("No @GoblinDatabaseConnection presented on ${clazz.name}")
    val name = annotation.name
    client = MongoClientManager.INSTANCE.getMongoClient(name)
        ?: throw GoblinDatabaseException("Mongo client [$name] not found")
  }

  fun getMongoClient(): MongoClient {
    return client
  }

  fun getNativeMongoClient(): com.mongodb.reactivestreams.client.MongoClient {
    return client.getNativeClient()
  }
}