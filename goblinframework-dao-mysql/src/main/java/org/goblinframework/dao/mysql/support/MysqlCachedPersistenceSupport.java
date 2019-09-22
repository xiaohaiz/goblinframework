package org.goblinframework.dao.mysql.support;

import org.goblinframework.cache.core.cache.GetResult;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

abstract public class MysqlCachedPersistenceSupport<E, ID> extends MysqlPersistenceSupport<E, ID> {

  private final GoblinCacheBean goblinCacheBean;
  private final org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution distribution;

  protected MysqlCachedPersistenceSupport() {
    this.goblinCacheBean = GoblinCacheBeanManager.getGoblinCacheBean(getClass());
    if (this.goblinCacheBean.isEmpty()) {
      distribution = org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.NONE;
    } else {
      org.goblinframework.cache.core.annotation.GoblinCacheDimension annotation = AnnotationUtils.findAnnotation(getClass(), org.goblinframework.cache.core.annotation.GoblinCacheDimension.class);
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

  private boolean hasIdCache() {
    return distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD
        || distribution == org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_AND_OTHER_FIELDS;
  }
}
