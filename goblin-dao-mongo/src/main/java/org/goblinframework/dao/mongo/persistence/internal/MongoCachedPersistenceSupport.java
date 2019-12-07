package org.goblinframework.dao.mongo.persistence.internal;

import org.goblinframework.cache.core.support.CacheBean;
import org.goblinframework.cache.core.support.CacheBeanManager;
import org.goblinframework.cache.core.support.CacheDimension;
import org.goblinframework.cache.core.support.GoblinCache;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.core.reactor.BlockingListSubscriber;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.core.annotation.GoblinCacheDimension;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;

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
abstract public class MongoCachedPersistenceSupport<E, ID> extends MongoPersistenceSupport<E, ID> {

  private final CacheBean cacheBean;
  private final GoblinCacheDimension.Dimension dimension;

  protected MongoCachedPersistenceSupport() {
    this.cacheBean = CacheBeanManager.getGoblinCacheBean(getClass());
    if (this.cacheBean.isEmpty()) {
      dimension = GoblinCacheDimension.Dimension.NONE;
    } else {
      GoblinCacheDimension annotation = AnnotationUtils.getAnnotation(getClass(), GoblinCacheDimension.class);
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

  abstract protected void calculateCacheDimensions(E document, CacheDimension dimension);

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
    Publisher<E> insertPublisher = __inserts(entities);
    new BlockingListSubscriber<E>().subscribe(insertPublisher).block();
    if (dimension == GoblinCacheDimension.Dimension.NONE) {
      return;
    }
    CacheDimension gcd = new CacheDimension(entityMapping.entityClass, cacheBean);
    entities.forEach(e -> calculateCacheDimensions(e, gcd));
    gcd.evict();
  }
}
