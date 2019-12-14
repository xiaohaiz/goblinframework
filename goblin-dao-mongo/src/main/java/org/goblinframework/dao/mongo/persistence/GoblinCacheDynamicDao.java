package org.goblinframework.dao.mongo.persistence;

import com.mongodb.MongoNamespace;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceCacheOperationSupport;
import org.jetbrains.annotations.NotNull;

abstract public class GoblinCacheDynamicDao<E, ID> extends MongoPersistenceCacheOperationSupport<E, ID> {

  protected GoblinCacheDynamicDao() {
    if (!isDynamicDatabase() && !isDynamicCollection()) {
      throw new GoblinMongoPersistenceException("Neither dynamic database nor dynamic collection found");
    }
  }

  @NotNull
  final protected MongoNamespace generateEntityNamespace(@NotNull E entity) {
    String database = getEntityDatabaseName(entity);
    String collection = getEntityCollectionName(entity);
    return new MongoNamespace(database, collection);
  }

  @NotNull
  final protected MongoNamespace generateIdNamespace(@NotNull ID id) {
    String database = getIdDatabaseName(id);
    String collection = getIdCollectionName(id);
    return new MongoNamespace(database, collection);
  }
}
