package org.goblinframework.dao.mongo.persistence;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.reactivestreams.client.FindPublisher;
import org.bson.BsonDocument;
import org.goblinframework.core.reactor.BlockingListSubscriber;
import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceCacheSupport;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;

import java.util.List;

abstract public class GoblinCacheStaticDao<E, ID> extends MongoPersistenceCacheSupport<E, ID> {

  protected GoblinCacheStaticDao() {
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
  public String calculateCollectionName(@NotNull String template, @NotNull E entity) {
    throw new UnsupportedOperationException();
  }

  @NotNull
  final protected MongoNamespace getNamespace() {
    String database = getEntityDatabaseName(null);
    String collection = getEntityCollectionName(null);
    return new MongoNamespace(database, collection);
  }

  final protected long __count() {
    return __count(ReadPreference.primary());
  }

  final protected long __count(@Nullable final ReadPreference readPreference) {
    return __count(readPreference, new Criteria());
  }

  final protected long __count(@NotNull final Criteria criteria) {
    return __count(ReadPreference.primary(), criteria);
  }

  final protected long __count(@Nullable final ReadPreference readPreference,
                               @NotNull final Criteria criteria) {
    Publisher<Long> publisher = __count(getNamespace(), readPreference, criteria);
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
  final protected List<E> __find() {
    return __find(ReadPreference.primary());
  }

  @NotNull
  final protected List<E> __find(@Nullable final ReadPreference readPreference) {
    Criteria criteria = new Criteria();
    Query query = Query.query(criteria);
    return __find(readPreference, query);
  }

  @NotNull
  final protected List<E> __find(@NotNull final Query query) {
    return __find(ReadPreference.primary(), query);
  }

  @NotNull
  final protected List<E> __find(@Nullable final ReadPreference readPreference,
                                 @NotNull final Query query) {
    FindPublisher<BsonDocument> findPublisher = __find(getNamespace(), readPreference, query);
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
