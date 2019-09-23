package org.goblinframework.dao.mysql.support;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.goblinframework.api.cache.GetResult;
import org.goblinframework.cache.core.support.GoblinCache;
import org.goblinframework.cache.core.support.GoblinCacheBean;
import org.goblinframework.cache.core.support.GoblinCacheBeanManager;
import org.goblinframework.cache.core.support.GoblinCacheDimension;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.mysql.module.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class MysqlCachedPersistenceSupport<E, ID> extends MysqlPersistenceSupport<E, ID> {

  private final GoblinCacheBean goblinCacheBean;
  private final org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution distribution;

  protected MysqlCachedPersistenceSupport() {
    this.goblinCacheBean = GoblinCacheBeanManager.getGoblinCacheBean(getClass());
    if (this.goblinCacheBean.isEmpty()) {
      distribution = org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE;
    } else {
      org.goblinframework.cache.core.annotation.GoblinCacheDimension annotation = AnnotationUtils.getAnnotation(getClass(), org.goblinframework.cache.core.annotation.GoblinCacheDimension.class);
      if (annotation == null) {
        String errMsg = "No @GoblinCacheDimension presented on %s";
        errMsg = String.format(errMsg, ClassUtils.filterCglibProxyClass(getClass()));
        throw new GoblinPersistenceException(errMsg);
      }
      distribution = annotation.value();
    }
  }

  protected String generateCacheKey(final ID id) {
    return CacheKeyGenerator.generateCacheKey(entityMapping.entityClass, id);
  }

  abstract protected void calculateCacheDimensions(E document, GoblinCacheDimension dimension);

  public GoblinCache getDefaultCache() {
    GoblinCache gc = goblinCacheBean.getGoblinCache(entityMapping.entityClass);
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
    directInserts(entities);
    if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE) {
      return;
    }
    GoblinCacheDimension gcd = new GoblinCacheDimension(entityMapping.entityClass, goblinCacheBean);
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
      return directLoads(getMasterConnection(), ids);
    }
    GoblinCache gc = getDefaultCache();
    return gc.cache.<ID, E>loader()
        .keyGenerator(this::generateCacheKey)
        .externalLoader(missed -> directLoads(getMasterConnection(), missed))
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
      return directExists(getMasterConnection(), id);
    }
    String idCacheKey = generateCacheKey(id);
    GoblinCache gc = getDefaultCache();
    GetResult<Object> gr = gc.cache.get(idCacheKey);
    if (gr.hit) {
      return gr.value != null;
    } else {
      return directExists(getMasterConnection(), id);
    }
  }

  @Override
  public boolean replace(@Nullable E entity) {
    if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE) {
      return directReplace(entity);
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
        E original = directLoad(getMasterConnection(), id);
        if (original == null) {
          return;
        }
        boolean ret = directReplace(entity);
        if (!ret) {
          return;
        }
        E replaced = directLoad(getMasterConnection(), id);
        assert replaced != null;
        if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD) {
          GoblinCache gc = getDefaultCache();
          gc.cache().modifier()
              .key(generateCacheKey(id))
              .expiration(gc.calculateExpiration())
              .modifier(currentValue -> replaced).execute();
        } else {
          GoblinCacheDimension gcd = new GoblinCacheDimension(entityMapping.entityClass, goblinCacheBean);
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
    if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE) {
      return directUpsert(entity);
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
        if (distribution != org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD) {
          original = directLoad(getMasterConnection(), id);
        }
        if (!directUpsert(entity)) {
          return;
        }
        E upserted = directLoad(getMasterConnection(), id);
        assert upserted != null;
        if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD) {
          GoblinCache gc = getDefaultCache();
          gc.cache().modifier()
              .key(generateCacheKey(id))
              .expiration(gc.calculateExpiration())
              .modifier(currentValue -> upserted).execute();
        } else {
          GoblinCacheDimension gcd = new GoblinCacheDimension(entityMapping.entityClass, goblinCacheBean);
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
    if (distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE) {
      return directDeletes(ids);
    }
    switch (distribution) {
      case ID_FIELD: {
        long deletedCount = directDeletes(ids);
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
            Map<ID, E> map = directLoads(getMasterConnection(), ids);
            long deletedCount = directDeletes(ids);
            if (deletedCount > 0) {
              GoblinCacheDimension gcd = new GoblinCacheDimension(entityMapping.entityClass, goblinCacheBean);
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
    return distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD
        || distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_AND_OTHER_FIELDS;
  }
}
