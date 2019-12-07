package org.goblinframework.database.mysql.support;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.goblinframework.cache.core.cache.GetResult;
import org.goblinframework.cache.core.support.CacheBean;
import org.goblinframework.cache.core.support.CacheBeanManager;
import org.goblinframework.cache.core.support.CacheDimension;
import org.goblinframework.cache.core.support.GoblinCache;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.core.annotation.GoblinCacheDimension;
import org.goblinframework.dao.mysql.persistence.internal.MysqlPersistenceOperationSupport;
import org.goblinframework.database.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class MysqlCachedPersistenceSupport<E, ID> extends MysqlPersistenceOperationSupport<E, ID> {

  private final CacheBean cacheBean;
  private final GoblinCacheDimension.Dimension dimension;

  protected MysqlCachedPersistenceSupport() {
    this.cacheBean = CacheBeanManager.getGoblinCacheBean(getClass());
    if (this.cacheBean.isEmpty()) {
      dimension = GoblinCacheDimension.Dimension.NONE;
    } else {
      GoblinCacheDimension annotation = AnnotationUtils.getAnnotation(getClass(), GoblinCacheDimension.class);
      if (annotation == null) {
        String errMsg = "No @GoblinCacheDimension presented on %s";
        errMsg = String.format(errMsg, ClassUtils.filterCglibProxyClass(getClass()));
        throw new GoblinPersistenceException(errMsg);
      }
      dimension = annotation.dimension();
    }
  }

  protected String generateCacheKey(final ID id) {
    return CacheKeyGenerator.generateCacheKey(entityMapping.entityClass, id);
  }

  abstract protected void calculateCacheDimensions(E document, CacheDimension dimension);

  public GoblinCache getDefaultCache() {
    GoblinCache gc = cacheBean.getGoblinCache(entityMapping.entityClass);
    if (gc == null) {
      String errMsg = "No @GoblinCacheBean of type (%s) presented";
      errMsg = String.format(errMsg, entityMapping.entityClass.getName());
      throw new GoblinPersistenceException(errMsg);
    }
    return gc;
  }

  @Override
  public void insert(@NotNull E entity) {
    inserts(Collections.singleton(entity));
  }

  @Override
  public void inserts(@Nullable Collection<E> entities) {
    if (entities == null || entities.isEmpty()) {
      return;
    }
    __inserts(entities);
    if (dimension == GoblinCacheDimension.Dimension.NONE) {
      return;
    }
    CacheDimension gcd = new CacheDimension(entityMapping.entityClass, cacheBean);
    entities.forEach(e -> calculateCacheDimensions(e, gcd));
    gcd.evict();
  }

  @Nullable
  @Override
  public E load(@Nullable ID id) {
    if (id == null) {
      return null;
    }
    return loads(Collections.singleton(id)).get(id);
  }

  @NotNull
  @Override
  public Map<ID, E> loads(@Nullable Collection<ID> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptyMap();
    }
    if (!hasIdCache()) {
      return __loads(getMasterConnection(), ids);
    }
    GoblinCache gc = getDefaultCache();
    return gc.cache.<ID, E>loader()
        .keyGenerator(this::generateCacheKey)
        .externalLoader(missed -> __loads(getMasterConnection(), missed))
        .keys(ids)
        .loads()
        .loadsMissed()
        .useValueWrapper(gc.wrapper)
        .expiration(gc.calculateExpiration())
        .write()
        .getAndResortResult();
  }

  @Override
  public boolean exists(@Nullable ID id) {
    if (id == null) return false;
    if (!hasIdCache()) {
      return __exists(getMasterConnection(), id);
    }
    String idCacheKey = generateCacheKey(id);
    GoblinCache gc = getDefaultCache();
    GetResult<Object> gr = gc.cache.get(idCacheKey);
    if (gr.hit) {
      return gr.value != null;
    } else {
      return __exists(getMasterConnection(), id);
    }
  }

  @Override
  public boolean replace(@Nullable E entity) {
    if (dimension == GoblinCacheDimension.Dimension.NONE) {
      return __replace(entity);
    }
    if (entity == null) return false;
    ID id = getEntityId(entity);
    if (id == null) {
      logger.warn("ID must not be null when executing replace operation.");
      return false;
    }
    MutableBoolean result = new MutableBoolean();
    getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        E original = __load(getMasterConnection(), id);
        if (original == null) {
          return;
        }
        boolean ret = __replace(entity);
        if (!ret) {
          return;
        }
        E replaced = __load(getMasterConnection(), id);
        assert replaced != null;
        if (dimension == GoblinCacheDimension.Dimension.ID_FIELD) {
          GoblinCache gc = getDefaultCache();
          gc.cache().modifier()
              .key(generateCacheKey(id))
              .expiration(gc.calculateExpiration())
              .modifier(currentValue -> replaced).execute();
        } else {
          CacheDimension gcd = new CacheDimension(entityMapping.entityClass, cacheBean);
          calculateCacheDimensions(original, gcd);
          calculateCacheDimensions(replaced, gcd);
          gcd.evict();
        }
        result.setTrue();
      }
    });
    return result.booleanValue();
  }

  @Override
  public boolean upsert(@Nullable E entity) {
    if (entity == null) return false;
    if (dimension == GoblinCacheDimension.Dimension.NONE) {
      return __upsert(entity);
    }
    ID id = getEntityId(entity);
    if (id == null) {
      insert(entity);
      return true;
    }
    MutableBoolean result = new MutableBoolean();
    getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        E original = null;
        if (dimension != GoblinCacheDimension.Dimension.ID_FIELD) {
          original = __load(getMasterConnection(), id);
        }
        if (!__upsert(entity)) {
          return;
        }
        E upserted = __load(getMasterConnection(), id);
        assert upserted != null;
        if (dimension == GoblinCacheDimension.Dimension.ID_FIELD) {
          GoblinCache gc = getDefaultCache();
          gc.cache().modifier()
              .key(generateCacheKey(id))
              .expiration(gc.calculateExpiration())
              .modifier(currentValue -> upserted).execute();
        } else {
          CacheDimension gcd = new CacheDimension(entityMapping.entityClass, cacheBean);
          if (original != null) {
            calculateCacheDimensions(original, gcd);
          }
          calculateCacheDimensions(upserted, gcd);
          gcd.evict();
        }
        result.setTrue();
      }
    });
    return result.booleanValue();
  }

  @Override
  public boolean delete(@Nullable ID id) {
    if (id == null) {
      return false;
    }
    return deletes(Collections.singleton(id)) > 0;
  }

  @Override
  public long deletes(@Nullable Collection<ID> ids) {
    if (ids == null || ids.isEmpty()) {
      return 0;
    }
    if (dimension == GoblinCacheDimension.Dimension.NONE) {
      return __deletes(ids);
    }
    switch (dimension) {
      case ID_FIELD: {
        long deletedCount = __deletes(ids);
        if (deletedCount > 0) {
          Set<String> keys = ids.stream()
              .map(this::generateCacheKey)
              .collect(Collectors.toSet());
          getDefaultCache().cache().deletes(keys);
        }
        return deletedCount;
      }
      case ID_AND_OTHER_FIELDS:
      case OTHER_FIELDS: {
        MutableLong result = new MutableLong();
        getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            Map<ID, E> map = __loads(getMasterConnection(), ids);
            long deletedCount = __deletes(ids);
            if (deletedCount > 0) {
              CacheDimension gcd = new CacheDimension(entityMapping.entityClass, cacheBean);
              map.values().forEach(e -> calculateCacheDimensions(e, gcd));
              gcd.evict();
            }
            result.setValue(deletedCount);
          }
        });
        return result.longValue();
      }
      default: {
        throw new UnsupportedOperationException();
      }
    }
  }

  private boolean hasIdCache() {
    return dimension == GoblinCacheDimension.Dimension.ID_FIELD
        || dimension == GoblinCacheDimension.Dimension.ID_AND_OTHER_FIELDS;
  }
}
