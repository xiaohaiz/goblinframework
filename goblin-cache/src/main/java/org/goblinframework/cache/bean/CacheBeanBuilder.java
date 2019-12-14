package org.goblinframework.cache.bean;

import org.goblinframework.cache.annotation.CacheBean;
import org.goblinframework.cache.annotation.CacheBeans;
import org.goblinframework.cache.exception.GoblinCacheException;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

class CacheBeanBuilder {

  @NotNull
  static GoblinCacheBean build(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    GoblinCacheBean cacheBean = generate(realClass);
    if (cacheBean == null) {
      return new GoblinCacheBean();
    }

    Map<Method, GoblinCacheMethod> methods = CacheMethodBuilder.build(realClass);
    cacheBean.methods.putAll(methods);
    return cacheBean;
  }

  private static GoblinCacheBean generate(Class<?> realClass) {
    List<CacheBean> annotations = new LinkedList<>();
    CacheBean cacheBean = AnnotationUtils.getAnnotation(realClass, CacheBean.class);
    if (cacheBean != null && cacheBean.enable()) {
      annotations.add(cacheBean);
    }
    CacheBeans cacheBeans = AnnotationUtils.getAnnotation(realClass, CacheBeans.class);
    if (cacheBeans != null) {
      Arrays.stream(cacheBeans.value()).filter(CacheBean::enable).forEach(annotations::add);
    }
    if (annotations.isEmpty()) {
      return null;
    }
    validateAnnotations(annotations, realClass);
    return new GoblinCacheBean(annotations);
  }

  private static void validateAnnotations(final List<CacheBean> annotations,
                                          final Class<?> realClass) {
    if (annotations.isEmpty()) {
      return;
    }
    IdentityHashMap<Class<?>, CacheBean> map = new IdentityHashMap<>();
    for (CacheBean annotation : annotations) {
      CacheBean previous = map.putIfAbsent(annotation.type(), annotation);
      if (previous != null) {
        String errMsg = "Duplicated cache type (%s) declared in @GoblinCacheBean of (%s)";
        errMsg = String.format(errMsg, annotation.type().getName(), realClass.getName());
        throw new GoblinCacheException(errMsg);
      }
    }
  }
}
