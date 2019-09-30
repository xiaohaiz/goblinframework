package org.goblinframework.database.mongo.support;

import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.database.mongo.reactor.SingleResultPublisher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

abstract public class MongoPersistenceSupport<E, ID> extends MongoConversionSupport<E, ID> {

  @NotNull
  final public Publisher<Collection<E>> __inserts(@Nullable Collection<E> entities) {
    if (entities == null || entities.isEmpty()) {
      SingleResultPublisher<Collection<E>> publisher = new SingleResultPublisher<>();
      publisher.complete(Collections.emptyList());
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

    groupEntities(entities).forEach((ns, es) -> {
      MongoDatabase database = getNativeMongoClient().getDatabase(ns.getDatabaseName());
      MongoCollection<BsonDocument> collection = database.getCollection(ns.getCollectionName(), BsonDocument.class);
      BsonArray array = (BsonArray) BsonConversionService.toBson(es);
      List<BsonDocument> docs = array.stream().map(e -> (BsonDocument) e).collect(Collectors.toList());
      CountDownLatch latch = new CountDownLatch(1);
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
              latch.countDown();
            }

            @Override
            public void onComplete() {
              latch.countDown();
            }
          });
      try {
        latch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    return null;
  }
}
