package org.goblinframework.dao.mongo.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.mongo.client.MongoClient;
import org.goblinframework.dao.mongo.client.MongoClientManager;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;

abstract public class MongoPersistenceConnectionSupport<E, ID> extends MongoPersistenceEntityMappingSupport<E, ID> {

  protected final com.mongodb.reactivestreams.client.MongoClient mongoClient;

  protected MongoPersistenceConnectionSupport() {
    PersistenceConnection annotation = AnnotationUtils.getAnnotation(getClass(), PersistenceConnection.class);
    if (annotation == null) {
      throw new GoblinMongoPersistenceException("No @PersistenceConnection presented");
    }
    String connection = annotation.connection();
    MongoClient client = MongoClientManager.INSTANCE.getMongoClient(connection);
    if (client == null) {
      throw new GoblinMongoPersistenceException("MongoClient [" + connection + "] not found");
    }
    this.mongoClient = client.getNativeClient();
  }
}
