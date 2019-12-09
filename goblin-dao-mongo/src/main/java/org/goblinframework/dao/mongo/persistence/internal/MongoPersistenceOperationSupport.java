package org.goblinframework.dao.mongo.persistence.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.goblinframework.core.reactor.BlockingListPublisher;
import org.goblinframework.core.reactor.BlockingListSubscriber;
import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.goblinframework.core.reactor.CoreScheduler;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.dao.mapping.EntityRevisionField;
import org.goblinframework.dao.mongo.bson.BsonConversionService;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.goblinframework.dao.ql.Update;
import org.goblinframework.database.mongo.eql.MongoCriteriaTranslator;
import org.goblinframework.database.mongo.eql.MongoQueryTranslator;
import org.goblinframework.database.mongo.eql.MongoUpdateTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.springframework.util.LinkedMultiValueMap;
import reactor.core.scheduler.Scheduler;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

abstract public class MongoPersistenceOperationSupport<E, ID> extends MongoPersistenceConversionSupport<E, ID> {

  protected final MongoCriteriaTranslator criteriaTranslator;
  protected final MongoQueryTranslator queryTranslator;
  protected final MongoUpdateTranslator updateTranslator;

  protected MongoPersistenceOperationSupport() {
    this.criteriaTranslator = MongoCriteriaTranslator.INSTANCE;
    this.queryTranslator = MongoQueryTranslator.INSTANCE;
    this.updateTranslator = MongoUpdateTranslator.INSTANCE;
  }

  public void insert(@NotNull final E entity) {
    __insert(entity);
  }

  public void inserts(@NotNull final Collection<E> entities) {
    __inserts(entities);
  }

  @Nullable
  public E load(@NotNull final ID id) {
    return __load(ReadPreference.primary(), id);
  }

  @NotNull
  public Map<ID, E> loads(@NotNull Collection<ID> ids) {
    return __loads(ReadPreference.primary(), ids);
  }

  public boolean exists(@NotNull final ID id) {
    return __exists(ReadPreference.primary(), id);
  }

  @Nullable
  public E replace(@NotNull final E entity) {
    return __replace(entity);
  }

  @NotNull
  public E upsert(@NotNull final E entity) {
    return __upsert(entity);
  }

  public boolean remove(@NotNull final ID id) {
    return __delete(id);
  }

  public long removes(@NotNull final Collection<ID> ids) {
    return __deletes(ids);
  }

  final public void __insert(@NotNull final E entity) {
    beforeInsert(entity);
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(entity);
    MongoNamespace namespace = getEntityNamespace(entity);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    Publisher<Success> insertPublisher = collection.insertOne(document);
    BlockingMonoSubscriber<Success> subscriber = new BlockingMonoSubscriber<>();
    insertPublisher.subscribe(subscriber);
    try {
      subscriber.block();
    } finally {
      subscriber.dispose();
    }
  }

  final public void __inserts(@NotNull final Collection<E> entities) {
    __inserts(entities, true);
  }

  final public void __inserts(@NotNull final Collection<E> entities,
                              boolean parallel) {
    List<E> entityList = entities.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (entityList.isEmpty()) {
      return;
    }
    if (entityList.size() == 1) {
      __insert(entityList.iterator().next());
      return;
    }
    entityList.forEach(this::beforeInsert);
    LinkedMultiValueMap<MongoNamespace, E> groupedEntities = groupEntities(entityList);
    if (groupedEntities.size() > 1 && parallel) {
      BlockingListPublisher<Success> publisher = new BlockingListPublisher<>(groupedEntities.size());
      groupedEntities.forEach((ns, es) -> {
        Scheduler scheduler = CoreScheduler.getInstance();
        scheduler.schedule(() -> {
          try {
            Publisher<Success> insertPublisher;
            if (es.size() == 1) {
              E entity = es.iterator().next();
              BsonDocument document = (BsonDocument) BsonConversionService.toBson(entity);
              insertPublisher = __insertOne(ns, document);
            } else {
              List<BsonDocument> documents = ((BsonArray) BsonConversionService.toBson(es)).stream()
                  .map(e -> (BsonDocument) e).collect(Collectors.toList());
              insertPublisher = __insertMany(ns, documents);
            }
            BlockingMonoSubscriber<Success> subscriber = new BlockingMonoSubscriber<>();
            insertPublisher.subscribe(subscriber);
            Success success;
            try {
              success = subscriber.block();
            } finally {
              subscriber.dispose();
            }
            publisher.onNext(success);
          } catch (Throwable ex) {
            publisher.onError(ex);
          } finally {
            publisher.onComplete();
          }
        });
      });
      publisher.block();
    } else {
      groupedEntities.forEach((ns, es) -> {
        Publisher<Success> insertPublisher;
        if (es.size() == 1) {
          E entity = es.iterator().next();
          BsonDocument document = (BsonDocument) BsonConversionService.toBson(entity);
          insertPublisher = __insertOne(ns, document);
        } else {
          List<BsonDocument> documents = ((BsonArray) BsonConversionService.toBson(es)).stream()
              .map(e -> (BsonDocument) e).collect(Collectors.toList());
          insertPublisher = __insertMany(ns, documents);
        }
        BlockingMonoSubscriber<Success> subscriber = new BlockingMonoSubscriber<>();
        insertPublisher.subscribe(subscriber);
        try {
          subscriber.block();
        } finally {
          subscriber.dispose();
        }
      });
    }
  }

  @Nullable
  final public E __load(@Nullable ReadPreference readPreference,
                        @NotNull final ID id) {
    MongoNamespace namespace = getIdNamespace(id);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    if (readPreference != null) {
      collection = collection.withReadPreference(readPreference);
    }
    Criteria criteria = Criteria.where("_id").is(id);
    Bson filter = criteriaTranslator.translate(criteria);
    FindPublisher<BsonDocument> findPublisher = collection.find(filter);
    BlockingListSubscriber<BsonDocument> subscriber = new BlockingListSubscriber<>();
    findPublisher.subscribe(subscriber);
    List<BsonDocument> documents;
    try {
      documents = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    BsonDocument document = documents.stream().findFirst().orElse(null);
    return convertBsonDocument(document);
  }

  @NotNull
  final public Map<ID, E> __loads(@Nullable ReadPreference readPreference,
                                  @NotNull final Collection<ID> ids) {
    return __loads(readPreference, ids, true);
  }

  @NotNull
  final public Map<ID, E> __loads(@Nullable ReadPreference readPreference,
                                  @NotNull final Collection<ID> ids,
                                  boolean parallel) {
    List<ID> idList = ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    if (idList.isEmpty()) {
      return Collections.emptyMap();
    }
    if (idList.size() == 1) {
      ID id = idList.iterator().next();
      E entity = __load(readPreference, id);
      if (entity == null) {
        return Collections.emptyMap();
      } else {
        Map<ID, E> entities = new LinkedHashMap<>();
        entities.put(id, entity);
        return entities;
      }
    }
    List<E> entityList;
    LinkedMultiValueMap<MongoNamespace, ID> groupedIds = groupIds(idList);
    if (groupedIds.size() > 1 && parallel) {
      BlockingListPublisher<E> publisher = new BlockingListPublisher<>(groupedIds.size());
      groupedIds.forEach((ns, ds) -> {
        Scheduler scheduler = CoreScheduler.getInstance();
        scheduler.schedule(() -> {
          try {
            Criteria criteria;
            if (ds.size() == 1) {
              ID id = ds.iterator().next();
              criteria = Criteria.where("_id").is(id);
            } else {
              criteria = Criteria.where("_id").in(ds);
            }
            Query query = Query.query(criteria);
            FindPublisher<BsonDocument> findPublisher = __find(ns, readPreference, query);
            BlockingListSubscriber<BsonDocument> subscriber = new BlockingListSubscriber<>();
            findPublisher.subscribe(subscriber);
            List<BsonDocument> documents;
            try {
              documents = subscriber.block();
            } finally {
              subscriber.dispose();
            }
            convertBsonDocuments(documents).forEach(publisher::onNext);
          } catch (Throwable ex) {
            publisher.onError(ex);
          } finally {
            publisher.onComplete();
          }
        });
      });
      entityList = publisher.block();
    } else {
      entityList = new ArrayList<>();
      groupedIds.forEach((ns, ds) -> {
        Criteria criteria;
        if (ds.size() == 1) {
          ID id = ds.iterator().next();
          criteria = Criteria.where("_id").is(id);
        } else {
          criteria = Criteria.where("_id").in(ds);
        }
        Query query = Query.query(criteria);
        FindPublisher<BsonDocument> findPublisher = __find(ns, readPreference, query);
        BlockingListSubscriber<BsonDocument> subscriber = new BlockingListSubscriber<>();
        findPublisher.subscribe(subscriber);
        List<BsonDocument> documents;
        try {
          documents = subscriber.block();
        } finally {
          subscriber.dispose();
        }
        entityList.addAll(convertBsonDocuments(documents));
      });
    }
    Map<ID, E> entities = new LinkedHashMap<>();
    entityList.forEach(e -> {
      ID id = getEntityId(e);
      entities.put(id, e);
    });
    return MapUtils.resort(entities, idList);
  }

  final public boolean __exists(@Nullable final ReadPreference readPreference,
                                @NotNull final ID id) {
    Criteria criteria = Criteria.where("_id").is(id);
    Bson filter = criteriaTranslator.translate(criteria);
    MongoNamespace namespace = getIdNamespace(id);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    if (readPreference != null) {
      collection = collection.withReadPreference(readPreference);
    }
    Publisher<Long> countPublisher = collection.countDocuments(filter);
    BlockingMonoSubscriber<Long> subscriber = new BlockingMonoSubscriber<>();
    countPublisher.subscribe(subscriber);
    Long count;
    try {
      count = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    return NumberUtils.toLong(count) > 0;
  }

  @Nullable
  final public E __replace(@NotNull final E entity) {
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMongoPersistenceException("Id must not be null when executing replace operation");
    }
    beforeReplace(entity);
    Criteria criteria = Criteria.where("_id").is(id);
    EntityRevisionField revisionField = entityMapping.revisionField;
    if (revisionField != null) {
      Object revision = revisionField.getField().get(entity);
      if (revision != null) {
        // revision specified, use it for optimistic concurrency checks
        criteria = criteria.and(revisionField.getName()).is(revision);
      }
    }
    Bson filter = criteriaTranslator.translate(criteria);
    Update update = new Update();
    entityMapping.updateTimeFields.forEach(ut -> {
      Object val = ut.getField().get(entity);
      if (val != null) {
        update.set(ut.getName(), val);
      }
    });
    entityMapping.normalFields.forEach(n -> {
      Object val = n.getField().get(entity);
      if (val != null) {
        update.set(n.getName(), val);
      }
    });
    if (revisionField != null) {
      update.inc(revisionField.getName(), 1);
    }
    if (update.export().isEmpty()) {
      throw new GoblinMongoPersistenceException("There is nothing field(s) found when executing replace operation");
    }

    MongoNamespace namespace = getIdNamespace(id);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(false).returnDocument(ReturnDocument.AFTER);
    Publisher<BsonDocument> replacePublisher = collection.findOneAndUpdate(filter, updateTranslator.translate(update), options);
    BlockingMonoSubscriber<BsonDocument> subscriber = new BlockingMonoSubscriber<>();
    replacePublisher.subscribe(subscriber);
    BsonDocument replaced;
    try {
      replaced = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    return convertBsonDocument(replaced);
  }

  @NotNull
  final public E __upsert(@NotNull final E entity) {
    ID id = getEntityId(entity);
    if (id == null) {
      // No id field specified, redirect to insert operation
      __insert(entity);
      return entity;
    }
    beforeInsert(entity);

    Criteria criteria = Criteria.where("_id").is(id);
    Bson filter = criteriaTranslator.translate(criteria);
    Update update = new Update();
    entityMapping.createTimeFields.forEach(ct -> {
      Object val = ct.getField().get(entity);
      if (val != null) {
        update.setOnInsert(ct.getName(), val);
      }
    });
    entityMapping.updateTimeFields.forEach(ut -> {
      Object val = ut.getField().get(entity);
      if (val != null) {
        update.set(ut.getName(), val);
      }
    });
    entityMapping.normalFields.forEach(n -> {
      Object val = n.getField().get(entity);
      if (val != null) {
        update.set(n.getName(), val);
      }
    });
    if (entityMapping.revisionField != null) {
      update.inc(entityMapping.revisionField.getName(), 1);
    }
    if (update.export().isEmpty()) {
      // There is nothing field(s) found when executing upsert operation, direct to insert
      __insert(entity);
      return entity;
    }
    MongoNamespace namespace = getIdNamespace(id);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
    Publisher<BsonDocument> upsertPublisher = collection.findOneAndUpdate(filter, updateTranslator.translate(update), options);
    BlockingMonoSubscriber<BsonDocument> subscriber = new BlockingMonoSubscriber<>();
    upsertPublisher.subscribe(subscriber);
    BsonDocument upserted;
    try {
      upserted = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    E result = convertBsonDocument(upserted);
    assert result != null;
    return result;
  }

  final public boolean __delete(@NotNull final ID id) {
    MongoNamespace namespace = getIdNamespace(id);
    Criteria criteria = Criteria.where("_id").is(id);
    Publisher<DeleteResult> deletePublisher = __deleteOne(namespace, criteria);
    BlockingMonoSubscriber<DeleteResult> subscriber = new BlockingMonoSubscriber<>();
    deletePublisher.subscribe(subscriber);
    DeleteResult deleteResult;
    try {
      deleteResult = subscriber.block();
    } finally {
      subscriber.dispose();
    }
    assert deleteResult != null;
    return deleteResult.getDeletedCount() > 0;
  }

  final public long __deletes(@NotNull final Collection<ID> ids) {
    return __deletes(ids, true);
  }

  final public long __deletes(@NotNull final Collection<ID> ids,
                              final boolean parallel) {
    List<ID> idList = ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    if (idList.isEmpty()) {
      return 0;
    }
    if (idList.size() == 1) {
      return __delete(idList.iterator().next()) ? 1 : 0;
    }
    AtomicLong deletedCount = new AtomicLong();
    LinkedMultiValueMap<MongoNamespace, ID> groupIds = groupIds(idList);
    if (groupIds.size() > 1 && parallel) {
      BlockingListPublisher<DeleteResult> publisher = new BlockingListPublisher<>(groupIds.size());
      groupIds.forEach((ns, ds) -> {
        Scheduler scheduler = CoreScheduler.getInstance();
        scheduler.schedule(() -> {
          try {
            Publisher<DeleteResult> deletePublisher;
            if (ds.size() == 1) {
              ID id = ds.iterator().next();
              Criteria criteria = Criteria.where("_id").is(id);
              deletePublisher = __deleteOne(ns, criteria);
            } else {
              Criteria criteria = Criteria.where("_id").in(ds);
              deletePublisher = __deleteMany(ns, criteria);
            }
            BlockingMonoSubscriber<DeleteResult> subscriber = new BlockingMonoSubscriber<>();
            deletePublisher.subscribe(subscriber);
            DeleteResult deleteResult;
            try {
              deleteResult = subscriber.block();
            } finally {
              subscriber.dispose();
            }
            publisher.onNext(deleteResult);
          } catch (Throwable ex) {
            publisher.onError(ex);
          } finally {
            publisher.onComplete();
          }
        });
      });
      deletedCount.set(publisher.block().stream().filter(Objects::nonNull).mapToLong(DeleteResult::getDeletedCount).sum());
    } else {
      groupIds.forEach((ns, ds) -> {
        Publisher<DeleteResult> deletePublisher;
        if (ds.size() == 1) {
          ID id = ds.iterator().next();
          Criteria criteria = Criteria.where("_id").is(id);
          deletePublisher = __deleteOne(ns, criteria);
        } else {
          Criteria criteria = Criteria.where("_id").in(ds);
          deletePublisher = __deleteMany(ns, criteria);
        }
        BlockingMonoSubscriber<DeleteResult> subscriber = new BlockingMonoSubscriber<>();
        deletePublisher.subscribe(subscriber);
        DeleteResult deleteResult;
        try {
          deleteResult = subscriber.block();
        } finally {
          subscriber.dispose();
        }
        assert deleteResult != null;
        deletedCount.addAndGet(NumberUtils.toLong(deleteResult.getDeletedCount()));
      });
    }
    return deletedCount.get();
  }

  @NotNull
  final public Publisher<Long> __count(@NotNull final MongoNamespace namespace,
                                       @Nullable final ReadPreference readPreference,
                                       @NotNull final Criteria criteria) {
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    if (readPreference != null) {
      collection = collection.withReadPreference(readPreference);
    }
    Bson filter = criteriaTranslator.translate(criteria);
    return collection.countDocuments(filter);
  }

  @NotNull
  final public FindPublisher<BsonDocument> __find(@NotNull final MongoNamespace namespace,
                                                  @Nullable final ReadPreference readPreference,
                                                  @NotNull final Query query) {
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    if (readPreference != null) {
      collection = collection.withReadPreference(readPreference);
    }
    Bson filter = queryTranslator.toFilter(query);
    FindPublisher<BsonDocument> findPublisher = collection.find(filter);
    if (query.getLimit() != null) {
      findPublisher = findPublisher.limit(query.getLimit());
    }
    if (query.getSkip() != null) {
      findPublisher = findPublisher.skip(query.getSkip());
    }
    Bson projection = queryTranslator.toProjection(query);
    if (projection != null) {
      findPublisher = findPublisher.projection(projection);
    }
    Bson sort = queryTranslator.toSort(query);
    if (sort != null) {
      findPublisher = findPublisher.sort(sort);
    }
    return findPublisher;
  }

  @NotNull
  final protected Publisher<Success> __insertOne(@NotNull final MongoNamespace namespace,
                                                 @NotNull final BsonDocument document) {
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    return collection.insertOne(document);
  }

  @NotNull
  final protected Publisher<Success> __insertMany(@NotNull final MongoNamespace namespace,
                                                  @NotNull final List<BsonDocument> documents) {
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    return collection.insertMany(documents);
  }

  final protected Publisher<DeleteResult> __deleteOne(@NotNull final MongoNamespace namespace,
                                                      @NotNull final Criteria criteria) {
    Bson filter = criteriaTranslator.translate(criteria);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    return collection.deleteOne(filter);
  }

  final protected Publisher<DeleteResult> __deleteMany(@NotNull final MongoNamespace namespace,
                                                       @NotNull final Criteria criteria) {
    Bson filter = criteriaTranslator.translate(criteria);
    MongoCollection<BsonDocument> collection = getMongoCollection(namespace);
    collection = collection.withWriteConcern(WriteConcern.ACKNOWLEDGED);
    return collection.deleteMany(filter);
  }
}
