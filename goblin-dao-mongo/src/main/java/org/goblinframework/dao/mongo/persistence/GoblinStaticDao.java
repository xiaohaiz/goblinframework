package org.goblinframework.dao.mongo.persistence;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.reactivestreams.client.FindPublisher;
import org.bson.BsonDocument;
import org.goblinframework.core.reactor.BlockingListSubscriber;
import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceOperationSupport;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;

import java.util.List;

abstract public class GoblinStaticDao<E, ID> extends MongoPersistenceOperationSupport<E, ID> {

  protected GoblinStaticDao() {
    if (isDynamicDatabase() || isDynamicCollection()) {
      throw new GoblinMongoPersistenceException("Dynamic database/collection not allowed");
    }
  }

  @Nullable
  @Override
  protected String calculateDatabaseName(@NotNull String template, @NotNull E entity) {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  protected String calculateCollectionName(@NotNull String template, @NotNull E entity) {
    throw new UnsupportedOperationException();
  }

  @NotNull
  final protected MongoNamespace getNamespace() {
    String database = getEntityDatabaseName(null);
    String collection = getEntityCollectionName(null);
    return new MongoNamespace(database, collection);
  }

  final public long __count() {
    return __count(new Criteria());
  }

  final public long __count(@NotNull final Criteria criteria) {
    Publisher<Long> publisher = __count(criteria, getNamespace(), ReadPreference.primary());
    BlockingMonoSubscriber<Long> subscriber = new BlockingMonoSubscriber<>();
    publisher.subscribe(subscriber);
    Long count;
    try {
      count = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    return NumberUtils.toLong(count);
  }

  @NotNull
  final public List<E> __find() {
    return __find(ReadPreference.primary());
  }

  @NotNull
  final public List<E> __find(@Nullable final ReadPreference readPreference) {
    Criteria criteria = new Criteria();
    Query query = Query.query(criteria);
    return __find(readPreference, query);
  }

  @NotNull
  final public List<E> __find(@NotNull final Query query) {
    return __find(ReadPreference.primary(), query);
  }

  @NotNull
  final public List<E> __find(@Nullable final ReadPreference readPreference,
                              @NotNull final Query query) {
    FindPublisher<BsonDocument> findPublisher = __find(query, getNamespace(), readPreference);
    BlockingListSubscriber<BsonDocument> subscriber = new BlockingListSubscriber<>();
    findPublisher.subscribe(subscriber);
    List<BsonDocument> documents;
    try {
      documents = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    return convertBsonDocuments(documents);
  }
}
