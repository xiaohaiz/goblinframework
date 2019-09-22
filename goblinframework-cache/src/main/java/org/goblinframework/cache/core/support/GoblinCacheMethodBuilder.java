package org.goblinframework.cache.core.support;

import org.goblinframework.cache.core.annotation.GoblinCacheParameter;
import org.goblinframework.core.cache.GoblinCacheException;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

class GoblinCacheMethodBuilder {

  @NotNull
  static Map<Method, GoblinCacheMethod> build(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    Map<Method, GoblinCacheMethod> map = new LinkedHashMap<>();
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
          GoblinCacheMethod gcm = generate(e);
          if (gcm != null) {
            map.put(e, gcm);
          }
        });

    return map;
  }

  @Nullable
  private static GoblinCacheMethod generate(@NotNull Method method) {
    org.goblinframework.cache.core.annotation.GoblinCacheMethod annotation = method.getAnnotation(org.goblinframework.cache.core.annotation.GoblinCacheMethod.class);
    if (annotation == null) {
      return null;
    }
    GoblinCacheMethod gcm = new GoblinCacheMethod();
    gcm.type = annotation.value();
    setCacheParameters(gcm, method);
    if (gcm.annotationCount == 0) {
      return null;
    }
    return gcm;
  }

  private static void setCacheParameters(@NotNull GoblinCacheMethod gcm,
                                         @NotNull Method method) {
    Annotation[][] methodParamAnnotations = method.getParameterAnnotations();
    for (int i = 0; i < methodParamAnnotations.length; i++) {
      Annotation[] paramAnnotations = methodParamAnnotations[i];
      GoblinCacheMethodParameter pmi = null;
      for (Annotation annotation : paramAnnotations) {
        if (annotation.annotationType() == GoblinCacheParameter.class) {
          pmi = new GoblinCacheMethodParameter();
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

    for (GoblinCacheMethodParameter pmi : gcm.parameterList) {
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
