package org.goblinframework.database.mongo.support;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.goblinframework.core.reactor.*;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.database.core.eql.Criteria;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.database.mongo.eql.MongoCriteriaTranslator;
import org.goblinframework.database.mongo.eql.MongoQueryTranslator;
import org.goblinframework.database.mongo.eql.MongoUpdateTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract public class MongoPersistenceSupport<E, ID> extends MongoConversionSupport<E, ID> {

  protected final MongoCriteriaTranslator criteriaTranslator;
  protected final MongoQueryTranslator queryTranslator;
  protected final MongoUpdateTranslator updateTranslator;

  protected MongoPersistenceSupport() {
    this.criteriaTranslator = MongoCriteriaTranslator.INSTANCE;
    this.queryTranslator = MongoQueryTranslator.INSTANCE;
    this.updateTranslator = MongoUpdateTranslator.INSTANCE;
  }

  private <T> SingleResultPublisher<T> createSingleResultPublisher() {
    return new SingleResultPublisher<>(CoreScheduler.INSTANCE.get());
  }

  private <T> MultipleResultsPublisher<T> createMultipleResultsPublisher() {
    return new MultipleResultsPublisher<>(CoreScheduler.INSTANCE.get());
  }

  public void insert(@Nullable E entity) {
    Publisher<E> publisher = __insert(entity);
    BlockingMonoSubscriber<E> subscriber = new BlockingMonoSubscriber<>();
    publisher.subscribe(subscriber);
    subscriber.block();
  }

  public void inserts(@Nullable Collection<E> entities) {
    Publisher<E> publisher = __inserts(entities);
    BlockingListSubscriber<E> subscriber = new BlockingListSubscriber<>();
    publisher.subscribe(subscriber);
    subscriber.block();
  }

  @Nullable
  public E load(@Nullable ID id) {
    Publisher<E> publisher = __load(id);
    BlockingMonoSubscriber<E> subscriber = new BlockingMonoSubscriber<>();
    publisher.subscribe(subscriber);
    return subscriber.block();
  }

  @NotNull
  public Map<ID, E> loads(@Nullable Collection<ID> ids) {
    Publisher<E> publisher = __loads(ids);
    BlockingListSubscriber<E> subscriber = new BlockingListSubscriber<>();
    publisher.subscribe(subscriber);
    Map<ID, E> result = subscriber.block().stream()
        .collect(Collectors.toMap(this::getEntityId, Function.identity()));
    return MapUtils.resort(result, ids);
  }

  @NotNull
  final public Publisher<E> __insert(@Nullable E entity) {
    if (entity == null) {
      SingleResultPublisher<E> publisher = createSingleResultPublisher();
      publisher.complete(null, null);
      return publisher;
    }
    return __inserts(Collections.singleton(entity));
  }

  @NotNull
  final public Publisher<E> __inserts(@Nullable Collection<E> entities) {
    MultipleResultsPublisher<E> publisher = createMultipleResultsPublisher();
    if (entities == null || entities.isEmpty()) {
      publisher.complete(null);
      return publisher;
    }

    long millis = System.currentTimeMillis();
    for (E entity : entities) {
      generateEntityId(entity);
      try {
        requireEntityId(entity);
      } catch (Exception ex) {
        publisher.complete(ex);
        return publisher;
      }
      touchCreateTime(entity, millis);
      touchUpdateTime(entity, millis);
      initializeRevision(entity);
    }

    LinkedMultiValueMap<MongoNamespace, E> grouped = groupEntities(entities);
    publisher.initializeCount(grouped.size());
    grouped.forEach((ns, es) -> {
      MongoDatabase database = getNativeMongoClient().getDatabase(ns.getDatabaseName());
      MongoCollection<BsonDocument> collection = database.getCollection(ns.getCollectionName(), BsonDocument.class);
      BsonArray array = (BsonArray) BsonConversionService.toBson(es);
      List<BsonDocument> docs = array.stream().map(e -> (BsonDocument) e).collect(Collectors.toList());
      if (docs.size() == 1) {
        collection.withWriteConcern(WriteConcern.ACKNOWLEDGED)
            .insertOne(docs.iterator().next())
            .subscribe(new Subscriber<Success>() {
              @Override
              public void onSubscribe(Subscription s) {
                s.request(1);
              }

              @Override
              public void onNext(Success success) {
                publisher.onNext(es.iterator().next());
              }

              @Override
              public void onError(Throwable t) {
                publisher.complete(t);
              }

              @Override
              public void onComplete() {
                publisher.release();
              }
            });
      } else {
        collection.withWriteConcern(WriteConcern.ACKNOWLEDGED)
            .insertMany(docs)
            .subscribe(new Subscriber<Success>() {
              @Override
              public void onSubscribe(Subscription s) {
                s.request(docs.size());
              }

              @Override
              public void onNext(Success success) {
              }

              @Override
              public void onError(Throwable t) {
                publisher.complete(t);
              }

              @Override
              public void onComplete() {
                es.forEach(publisher::onNext);
                publisher.release();
              }
            });
      }
    });
    return publisher;
  }

  @NotNull
  final public Publisher<E> __load(@Nullable ID id) {
    if (id == null) {
      SingleResultPublisher<E> publisher = createSingleResultPublisher();
      publisher.complete(null, null);
      return publisher;
    }
    return __loads(Collections.singleton(id));
  }

  @NotNull
  final public Publisher<E> __loads(@Nullable Collection<ID> ids) {
    MultipleResultsPublisher<E> publisher = createMultipleResultsPublisher();
    if (ids == null || ids.isEmpty()) {
      publisher.complete(null);
      return publisher;
    }
    LinkedMultiValueMap<MongoNamespace, ID> grouped = groupIds(ids);
    publisher.initializeCount(grouped.size());
    grouped.forEach((ns, ds) -> {
      MongoDatabase database = getNativeMongoClient().getDatabase(ns.getDatabaseName());
      MongoCollection<BsonDocument> collection = database.getCollection(ns.getCollectionName(), BsonDocument.class);
      Criteria criteria;
      if (ds.size() == 1) {
        criteria = Criteria.where("_id").is(ds.iterator().next());
      } else {
        criteria = Criteria.where("_id").in(ds);
      }
      Bson filter = criteriaTranslator.translate(criteria);
      FindPublisher<BsonDocument> findPublisher = collection.find(filter, BsonDocument.class);
      findPublisher.subscribe(new Subscriber<BsonDocument>() {
        @Override
        public void onSubscribe(Subscription s) {
          s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(BsonDocument bsonDocument) {
          try {
            E loaded = convertBsonDocument(bsonDocument);
            Objects.requireNonNull(loaded);
            publisher.onNext(loaded);
          } catch (Exception ex) {
            publisher.complete(ex);
          }
        }

        @Override
        public void onError(Throwable t) {
          publisher.complete(t);
        }

        @Override
        public void onComplete() {
          publisher.release();
        }
      });
    });

    return publisher;
  }
}
