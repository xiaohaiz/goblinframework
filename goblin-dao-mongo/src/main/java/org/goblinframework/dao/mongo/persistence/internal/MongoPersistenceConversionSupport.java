package org.goblinframework.dao.mongo.persistence.internal;

import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.jetbrains.annotations.Nullable;

abstract public class MongoPersistenceConversionSupport<E, ID> extends MongoPersistencePrimaryKeySupport<E, ID> {

  protected MongoPersistenceConversionSupport() {
  }

  @Nullable
  protected E convertBsonDocument(@Nullable BsonDocument document) {
    if (document == null) {
      return null;
    }
    return BsonConversionService.toObject(document, getEntityClass());
  }
}
