package org.goblinframework.dao.mongo.persistence

import com.mongodb.MongoNamespace
import com.mongodb.ReadPreference
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceOperationSupport
import org.goblinframework.dao.ql.Query

abstract class GoblinStaticDao<E, ID> : MongoPersistenceOperationSupport<E, ID>() {

  override fun calculateDatabaseName(template: String, entity: E): String? {
    throw UnsupportedOperationException()
  }

  override fun calculateCollectionName(template: String, entity: E): String? {
    throw UnsupportedOperationException()
  }

  fun getDatabaseName(): String {
    return getEntityDatabaseName(null)
  }

  fun getDatabase(): MongoDatabase {
    val databaseName = getDatabaseName()
    return mongoClient.getDatabase(databaseName)
  }

  fun getCollectionName(): String {
    return getEntityCollectionName(null)
  }

  fun getCollection(): MongoCollection<BsonDocument> {
    val collectionName = getCollectionName()
    return getDatabase().getCollection(collectionName, BsonDocument::class.java)
  }

  fun getNamespace(): MongoNamespace {
    return MongoNamespace(getDatabaseName(), getCollectionName())
  }

  fun executeQuery(query: Query, readPreference: ReadPreference?): List<E> {
    return __query(query, getNamespace(), readPreference)
  }
}