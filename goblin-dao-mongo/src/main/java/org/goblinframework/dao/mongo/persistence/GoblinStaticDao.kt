package org.goblinframework.dao.mongo.persistence

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.goblinframework.database.mongo.support.MongoPersistenceSupport

abstract class GoblinStaticDao<E, ID> : MongoPersistenceSupport<E, ID>() {

  override fun calculateDatabaseName(template: String, entity: E): String {
    throw UnsupportedOperationException()
  }

  override fun calculateCollectionName(template: String, entity: E): String {
    throw UnsupportedOperationException()
  }

  fun getDatabaseName(): String {
    return getEntityDatabaseName(null)
  }

  fun getDatabase(): MongoDatabase {
    val databaseName = getDatabaseName()
    return getNativeMongoClient().getDatabase(databaseName)
  }

  fun getCollectionName(): String {
    return getEntityCollectionName(null)
  }

  fun getCollection(): MongoCollection<BsonDocument> {
    val collectionName = getCollectionName()
    return getDatabase().getCollection(collectionName, BsonDocument::class.java)
  }
}