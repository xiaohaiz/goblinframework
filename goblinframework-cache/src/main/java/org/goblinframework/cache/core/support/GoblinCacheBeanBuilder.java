package org.goblinframework.cache.core.support;

import org.goblinframework.api.cache.GoblinCacheException;
import org.goblinframework.cache.core.annotation.GoblinCacheBeans;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

class GoblinCacheBeanBuilder {

  @NotNull
  static GoblinCacheBean build(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    GoblinCacheBean cacheBean = generate(realClass);
    if (cacheBean == null) {
      return new GoblinCacheBean();
    }

    Map<Method, GoblinCacheMethod> methods = GoblinCacheMethodBuilder.build(realClass);
    cacheBean.methods.putAll(methods);
    return cacheBean;
  }

  private static GoblinCacheBean generate(Class<?> realClass) {
    List<org.goblinframework.cache.core.annotation.GoblinCacheBean> annotations = new LinkedList<>();
    org.goblinframework.cache.core.annotation.GoblinCacheBean cacheBean = AnnotationUtils.getAnnotation(realClass, org.goblinframework.cache.core.annotation.GoblinCacheBean.class);
    if (cacheBean != null && cacheBean.enable()) {
      annotations.add(cacheBean);
    }
    GoblinCacheBeans cacheBeans = AnnotationUtils.getAnnotation(realClass, GoblinCacheBeans.class);
    if (cacheBeans != null) {
      Arrays.stream(cacheBeans.value()).filter(org.goblinframework.cache.core.annotation.GoblinCacheBean::enable).forEach(annotations::add);
    }
    if (annotations.isEmpty()) {
      return null;
    }
    validateAnnotations(annotations, realClass);
    return new GoblinCacheBean(annotations);
  }

  private static void validateAnnotations(final List<org.goblinframework.cache.core.annotation.GoblinCacheBean> annotations,
                                          final Class<?> realClass) {
    if (annotations.isEmpty()) {
      return;
    }
    IdentityHashMap<Class<?>, org.goblinframework.cache.core.annotation.GoblinCacheBean> map = new IdentityHashMap<>();
    for (org.goblinframework.cache.core.annotation.GoblinCacheBean annotation : annotations) {
      org.goblinframework.cache.core.annotation.GoblinCacheBean previous = map.putIfAbsent(annotation.type(), annotation);
      if (previous != null) {
        String errMsg = "Duplicated cache type (%s) declared in @GoblinCacheBean of (%s)";
        errMsg = String.format(errMsg, annotation.type().getName(), realClass.getName());
        throw new GoblinCacheException(errMsg);
      }
    }
  }
}
