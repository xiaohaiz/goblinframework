package org.goblinframework.cache.core.support;

import org.goblinframework.api.cache.GoblinCacheBeans;
import org.goblinframework.cache.core.GoblinCacheException;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

class CacheBeanBuilder {

  @NotNull
  static CacheBean build(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    CacheBean cacheBean = generate(realClass);
    if (cacheBean == null) {
      return new CacheBean();
    }

    Map<Method, CacheMethod> methods = CacheMethodBuilder.build(realClass);
    cacheBean.methods.putAll(methods);
    return cacheBean;
  }

  private static CacheBean generate(Class<?> realClass) {
    List<org.goblinframework.api.cache.GoblinCacheBean> annotations = new LinkedList<>();
    org.goblinframework.api.cache.GoblinCacheBean cacheBean = AnnotationUtils.getAnnotation(realClass, org.goblinframework.api.cache.GoblinCacheBean.class);
    if (cacheBean != null && cacheBean.enable()) {
      annotations.add(cacheBean);
    }
    GoblinCacheBeans cacheBeans = AnnotationUtils.getAnnotation(realClass, GoblinCacheBeans.class);
    if (cacheBeans != null) {
      Arrays.stream(cacheBeans.value()).filter(org.goblinframework.api.cache.GoblinCacheBean::enable).forEach(annotations::add);
    }
    if (annotations.isEmpty()) {
      return null;
    }
    validateAnnotations(annotations, realClass);
    return new CacheBean(annotations);
  }

  private static void validateAnnotations(final List<org.goblinframework.api.cache.GoblinCacheBean> annotations,
                                          final Class<?> realClass) {
    if (annotations.isEmpty()) {
      return;
    }
    IdentityHashMap<Class<?>, org.goblinframework.api.cache.GoblinCacheBean> map = new IdentityHashMap<>();
    for (org.goblinframework.api.cache.GoblinCacheBean annotation : annotations) {
      org.goblinframework.api.cache.GoblinCacheBean previous = map.putIfAbsent(annotation.type(), annotation);
      if (previous != null) {
        String errMsg = "Duplicated cache type (%s) declared in @GoblinCacheBean of (%s)";
        errMsg = String.format(errMsg, annotation.type().getName(), realClass.getName());
        throw new GoblinCacheException(errMsg);
      }
    }
  }
}
