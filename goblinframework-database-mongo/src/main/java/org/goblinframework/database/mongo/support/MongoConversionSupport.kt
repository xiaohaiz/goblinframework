package org.goblinframework.database.mongo.support

import org.bson.BsonDocument
import org.goblinframework.database.mongo.bson.BsonConversionService

abstract class MongoConversionSupport<E, ID> : MongoPrimaryKeySupport<E, ID>() {

  fun convertBsonDocument(doc: BsonDocument?): E? {
    return doc?.run {
      @Suppress("UNCHECKED_CAST")
      BsonConversionService.toObject(doc, entityMapping.entityClass) as E
    }
  }
}