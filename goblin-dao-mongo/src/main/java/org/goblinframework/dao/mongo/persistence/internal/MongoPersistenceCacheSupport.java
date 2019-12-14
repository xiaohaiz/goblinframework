package org.goblinframework.dao.mongo.persistence.internal;

import org.goblinframework.cache.bean.GoblinCache;
import org.goblinframework.cache.bean.GoblinCacheBean;
import org.goblinframework.cache.bean.GoblinCacheBeanManager;
import org.goblinframework.cache.bean.GoblinCacheDimension;
import org.goblinframework.cache.util.CacheKeyGenerator;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.annotation.PersistenceCacheDimension;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
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
  }

  protected String generateCacheKey(final ID id) {
    return CacheKeyGenerator.generateCacheKey(entityMapping.entityClass, id);
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
    inserts(Collections.singleton(entity));
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
    GoblinCacheDimension gcd = new GoblinCacheDimension(entityMapping.entityClass, cacheBean);
    entities.forEach(e -> calculateCacheDimensions(e, gcd));
    gcd.evict();
  }
}
