package org.goblinframework.cache.core.support;

import org.goblinframework.cache.annotation.GoblinCacheMethod;
import org.goblinframework.cache.core.annotation.GoblinCacheParameter;
import org.goblinframework.cache.exception.GoblinCacheException;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

class CacheMethodBuilder {

  @NotNull
  static Map<Method, CacheMethod> build(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    Map<Method, CacheMethod> map = new LinkedHashMap<>();
    Arrays.stream(realClass.getDeclaredMethods())
        .filter(e -> {
          int mod = e.getModifiers();
          return !Modifier.isStatic(mod);
        })
        .filter(e -> {
          int mod = e.getModifiers();
          return !Modifier.isFinal(mod);
        })
        .filter(e -> {
          int mod = e.getModifiers();
          return !Modifier.isPrivate(mod);
        })
        .forEach(e -> {
          CacheMethod gcm = generate(e);
          if (gcm != null) {
            map.put(e, gcm);
          }
        });

    return map;
  }

  @Nullable
  private static CacheMethod generate(@NotNull Method method) {
    GoblinCacheMethod annotation = method.getAnnotation(GoblinCacheMethod.class);
    if (annotation == null) {
      return null;
    }
    CacheMethod gcm = new CacheMethod();
    gcm.type = annotation.value();
    setCacheParameters(gcm, method);
    if (gcm.annotationCount == 0) {
      return null;
    }
    return gcm;
  }

  private static void setCacheParameters(@NotNull CacheMethod gcm,
                                         @NotNull Method method) {
    Annotation[][] methodParamAnnotations = method.getParameterAnnotations();
    for (int i = 0; i < methodParamAnnotations.length; i++) {
      Annotation[] paramAnnotations = methodParamAnnotations[i];
      CacheMethodParameter pmi = null;
      for (Annotation annotation : paramAnnotations) {
        if (annotation.annotationType() == GoblinCacheParameter.class) {
          pmi = new CacheMethodParameter();
          pmi.name = ((GoblinCacheParameter) annotation).value();
          pmi.multiple = ((GoblinCacheParameter) annotation).multiple();
          break;
        }
      }
      if (pmi != null) {
        pmi.index = i;
        gcm.parameterList.add(pmi);
      }
    }

    int annotationCount = 0;
    int multipleCount = 0;
    int multipleIndex = -1;

    for (CacheMethodParameter pmi : gcm.parameterList) {
      annotationCount++;
      if (pmi.multiple) {
        multipleCount++;
        multipleIndex = pmi.index;
      }
    }

    if (multipleCount > 1) {
      throw new GoblinCacheException("At most 1 multiple CacheParameter");
    }
    if (multipleCount == 1) {
      Class<?> multipleParameterType = method.getParameterTypes()[multipleIndex];
      if (multipleParameterType != Collection.class
          && multipleParameterType != Set.class
          && multipleParameterType != List.class) {
        String errMsg = "The multiple cache parameter type must be Collection/List/Set";
        throw new GoblinCacheException(errMsg);
      }
    }
    if (multipleCount == 1 && method.getReturnType() != Map.class) {
      String errMsg = "The result type of method which has multiple cache parameter must be Map";
      throw new GoblinCacheException(errMsg);
    }

    gcm.annotationCount = annotationCount;
    gcm.multipleCount = multipleCount;
    gcm.multipleIndex = multipleIndex;
    gcm.parameterCount = method.getParameterCount();
  }
}
