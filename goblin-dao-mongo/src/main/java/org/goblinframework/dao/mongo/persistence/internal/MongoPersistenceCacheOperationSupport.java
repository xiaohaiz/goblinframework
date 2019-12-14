package org.goblinframework.dao.mongo.persistence.internal;

import com.mongodb.ReadPreference;
import org.goblinframework.cache.bean.GoblinCache;
import org.goblinframework.cache.bean.GoblinCacheBean;
import org.goblinframework.cache.bean.GoblinCacheBeanManager;
import org.goblinframework.cache.bean.GoblinCacheDimension;
import org.goblinframework.cache.core.CacheKeyGenerator;
import org.goblinframework.cache.core.CacheValueWrapper;
import org.goblinframework.cache.core.GetResult;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.annotation.PersistenceCacheDimension;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mongo persistence with cache support.
 *
 * @author Xiaohai Zhang
 * @since Dec 6, 2019
 */
abstract public class MongoPersistenceCacheOperationSupport<E, ID> extends MongoPersistenceOperationSupport<E, ID> {

  private final GoblinCacheBean cacheBean;
  private final PersistenceCacheDimension.Dimension dimension;
  private final boolean cacheOnId;

  protected MongoPersistenceCacheOperationSupport() {
    this.cacheBean = GoblinCacheBeanManager.getGoblinCacheBean(getClass());
    if (this.cacheBean.isEmpty()) {
      dimension = PersistenceCacheDimension.Dimension.NONE;
    } else {
      PersistenceCacheDimension annotation = AnnotationUtils.getAnnotation(getClass(), PersistenceCacheDimension.class);
      if (annotation == null) {
        String errMsg = "No @GoblinCacheDimension presented on %s";
        errMsg = String.format(errMsg, ClassUtils.filterCglibProxyClass(getClass()));
        throw new IllegalArgumentException(errMsg);
      }
      dimension = annotation.dimension();
    }
    cacheOnId = dimension == PersistenceCacheDimension.Dimension.ID_FIELD
        || dimension == PersistenceCacheDimension.Dimension.ID_AND_OTHER_FIELDS;
  }

  protected String generateCacheKey(final ID id) {
    return CacheKeyGenerator.generateCacheKey(entityMapping.entityClass, id);
  }

  protected GoblinCacheDimension createCacheDimension() {
    return new GoblinCacheDimension(getEntityClass(), cacheBean);
  }

  abstract protected void calculateCacheDimensions(E document, GoblinCacheDimension dimension);

  public GoblinCache getDefaultCache() {
    GoblinCache gc = cacheBean.getGoblinCache(entityMapping.entityClass);
    if (gc == null) {
      String errMsg = "No @GoblinCacheBean of type (%s) presented";
      errMsg = String.format(errMsg, entityMapping.entityClass.getName());
      throw new IllegalArgumentException(errMsg);
    }
    return gc;
  }

  @Override
  public void insert(@NotNull E entity) {
    __insert(entity);
    if (dimension == PersistenceCacheDimension.Dimension.NONE) {
      return;
    }
    GoblinCacheDimension cacheDimension = createCacheDimension();
    calculateCacheDimensions(entity, cacheDimension);
    cacheDimension.evict();
  }

  @Override
  public void inserts(@NotNull Collection<E> entities) {
    List<E> candidates = entities.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (candidates.isEmpty()) {
      return;
    }
    __inserts(entities);
    if (dimension == PersistenceCacheDimension.Dimension.NONE) {
      return;
    }
    GoblinCacheDimension cacheDimension = createCacheDimension();
    entities.forEach(e -> calculateCacheDimensions(e, cacheDimension));
    cacheDimension.evict();
  }

  @Nullable
  @Override
  public E load(@NotNull ID id) {
    if (!cacheOnId) {
      return __load(ReadPreference.primary(), id);
    }
    GoblinCache defaultCache = getDefaultCache();
    String key = generateCacheKey(id);
    GetResult<E> getResult = defaultCache.cache().get(key);
    if (getResult.hit) {
      return getResult.value;
    }
    E entity = __load(ReadPreference.primary(), id);
    Object candidate = entity;
    if (candidate == null && defaultCache.wrapper) {
      candidate = new CacheValueWrapper(null);
    }
    if (candidate != null) {
      int expiration = defaultCache.calculateExpiration();
      defaultCache.cache().add(key, expiration, candidate);
    }
    return entity;
  }

  @NotNull
  @Override
  public Map<ID, E> loads(@NotNull Collection<ID> ids) {
    List<ID> idList = ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    if (idList.isEmpty()) {
      return Collections.emptyMap();
    }
    if (!cacheOnId) {
      return __loads(ReadPreference.primary(), idList);
    }
    GoblinCache defaultCache = getDefaultCache();
    return defaultCache.cache().<ID, E>loader()
        .keyGenerator(this::generateCacheKey)
        .externalLoader(missed -> __loads(ReadPreference.primary(), missed))
        .keys(idList)
        .loads()
        .loadsMissed()
        .useValueWrapper(defaultCache.wrapper)
        .expiration(defaultCache.calculateExpiration())
        .write()
        .getAndResortResult();
  }

  @Override
  public boolean exists(@NotNull ID id) {
    if (!cacheOnId) {
      return __exists(ReadPreference.primary(), id);
    }
    String key = generateCacheKey(id);
    GoblinCache defaultCache = getDefaultCache();
    GetResult<E> getResult = defaultCache.cache().get(key);
    if (getResult.hit) {
      return getResult.value != null;
    } else {
      return __exists(ReadPreference.primary(), id);
    }
  }

  @Nullable
  @Override
  public E replace(@NotNull E entity) {
    if (dimension == PersistenceCacheDimension.Dimension.NONE) {
      return __replace(entity);
    }
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMongoPersistenceException("Id must not be null when executing replace operation");
    }
    E original = __load(ReadPreference.primary(), id);
    if (original == null) {
      return null;
    }
    E modified = __replace(entity);
    if (modified == null) {
      return null;
    }
    GoblinCacheDimension cacheDimension = createCacheDimension();
    calculateCacheDimensions(original, cacheDimension);
    calculateCacheDimensions(modified, cacheDimension);
    cacheDimension.evict();
    return modified;
  }
}
