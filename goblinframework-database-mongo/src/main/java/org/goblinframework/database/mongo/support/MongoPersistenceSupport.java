package org.goblinframework.database.mongo.support;

import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.database.mongo.reactor.MultipleResultsPublisher;
import org.goblinframework.database.mongo.reactor.SingleResultPublisher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

abstract public class MongoPersistenceSupport<E, ID> extends MongoConversionSupport<E, ID> {

  @NotNull
  final public Publisher<E> __insert(@Nullable E entity) {
    if (entity == null) {
      SingleResultPublisher<E> publisher = new SingleResultPublisher<>();
      publisher.complete(null, null);
      return publisher;
    }
    return __inserts(Collections.singleton(entity));
  }

  @NotNull
  final public Publisher<E> __inserts(@Nullable Collection<E> entities) {
    MultipleResultsPublisher<E> publisher = new MultipleResultsPublisher<>();
    if (entities == null || entities.isEmpty()) {
      publisher.complete(null);
      return publisher;
    }

    long millis = System.currentTimeMillis();
    for (E entity : entities) {
      //generateEntityId(entity);
      //requireEntityId(entity);
      touchCreateTime(entity, millis);
      touchUpdateTime(entity, millis);
      initializeRevision(entity);
    }

    publisher.initializeCount(entities.size());

    groupEntities(entities).forEach((ns, es) -> {
      MongoDatabase database = getNativeMongoClient().getDatabase(ns.getDatabaseName());
      MongoCollection<BsonDocument> collection = database.getCollection(ns.getCollectionName(), BsonDocument.class);
      BsonArray array = (BsonArray) BsonConversionService.toBson(es);
      List<BsonDocument> docs = array.stream().map(e -> (BsonDocument) e).collect(Collectors.toList());
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
              es.forEach(e -> {
                publisher.onNext(e);
                publisher.release();
              });
            }
          });
    });
    return publisher;
  }
}
