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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mongo persistence with cache support.
 *
 * @author Xiaohai Zhang
 * @since Dec 6, 2019
 */
abstract public class MongoPersistenceCacheSupport<E, ID> extends MongoPersistenceOperationSupport<E, ID> {

  private final GoblinCacheBean cacheBean;
  private final PersistenceCacheDimension.Dimension dimension;
  private final boolean cacheOnId;

  protected MongoPersistenceCacheSupport() {
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
}
