package org.goblinframework.cache.core.enhance;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.api.cache.*;
import org.goblinframework.cache.core.support.GoblinCache;
import org.goblinframework.cache.core.support.GoblinCacheBean;
import org.goblinframework.cache.core.support.GoblinCacheMethod;
import org.goblinframework.cache.core.support.GoblinCacheMethodParameter;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.core.util.CollectionUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

final class GoblinCacheInterceptor implements MethodInterceptor {

  @NotNull private final Object target;
  @NotNull private final GoblinCacheBean capsule;

  GoblinCacheInterceptor(@NotNull Object target, @NotNull GoblinCacheBean capsule) {
    this.target = target;
    this.capsule = capsule;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "GoblinCacheInterceptor(" + target + ")";
    }
    Object[] arguments = invocation.getArguments();

    GoblinCacheMethod gcm = capsule.getGoblinCacheMethod(method);
    if (gcm == null) {
      return ReflectionUtils.invoke(target, method, arguments);
    }

    Class<?> type = gcm.type;
    GoblinCache gc = capsule.getGoblinCache(type);
    if (gc == null) {
      throw new GoblinCacheException("@GoblinCacheBean(s) missed: " + type.getName());
    }
    if (gcm.multipleCount == 0) {
      String ck = generateSingleCacheKey(gcm, arguments);
      GetResult<Object> cached = gc.cache().get(ck);
      if (cached.hit) {
        onCacheHit(ck);
        return cached.value;
      }

      onCacheMiss(ck);
      Object realValue = ReflectionUtils.invoke(target, method, arguments);
      Object candidate = realValue;
      if (candidate == null && gc.wrapper) {
        candidate = new CacheValueWrapper(null);
      }
      if (candidate != null) {
        int expiration = gc.calculateExpiration();
        gc.cache().add(ck, expiration, candidate);
      }
      return realValue;
    } else {
      int index = gcm.multipleIndex;
      if (arguments[index] == null) {
        return ReflectionUtils.invoke(target, method, arguments);
      }
      Collection collection = (Collection) arguments[index];
      if (CollectionUtils.isEmpty(collection)) {
        // return empty map directly
        return Collections.emptyMap();
      }

      KeyGenerator<Object> keyGenerator = each -> {
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (GoblinCacheMethodParameter pmi : gcm.parameterList) {
          keys.add(pmi.name);
          if (pmi.multiple) {
            values.add(each);
          } else {
            values.add(arguments[pmi.index]);
          }
        }
        return CacheKeyGenerator.generateCacheKey(gcm.type,
            keys.toArray(new String[0]),
            values.toArray(new Object[0]));
      };
      ExternalLoader<Object, Object> externalLoader = missedSources -> {
        List<Object> modified = new ArrayList<>();
        for (int i = 0; i < arguments.length; i++) {
          if (i != index) {
            modified.add(arguments[i]);
          } else {
            Class<?> t = method.getParameterTypes()[index];
            if (t == Collection.class) {
              modified.add(missedSources);
            } else if (t == List.class) {
              modified.add(new LinkedList(missedSources));
            } else if (t == Set.class) {
              modified.add(new LinkedHashSet(missedSources));
            } else {
              throw new UnsupportedOperationException();
            }
          }
        }
        Object[] arr = modified.toArray(new Object[0]);
        try {
          return (Map<Object, Object>) ReflectionUtils.invoke(target, method, arr);
        } catch (Throwable throwable) {
          throw new InternalInvocationTargetException(throwable);
        }
      };

      Class<?> elementType = null;
      Type grt = method.getGenericReturnType();
      if (grt instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) grt;
        Type[] ta = pt.getActualTypeArguments();
        if (ta != null && ta.length == 2) {
          Type t = ta[1];
          if (t instanceof Class) {
            elementType = (Class<?>) t;
          } else if (t instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) t).getRawType();
            if (rawType instanceof Class) {
              elementType = (Class<?>) rawType;
            }
          }
        }
      }
      try {
        Object defaultValue = null;
        CacheValueLoader<Object, Object> loader = gc.cache().loader();
        if (target instanceof CacheHitListener) {
          loader = loader.cacheHitListener((CacheHitListener) target);
        }
        if (target instanceof CacheMissListener) {
          loader = loader.cacheMissListener((CacheMissListener) target);
        }
        loader = loader.keyGenerator(keyGenerator)
            .externalLoader(externalLoader)
            .keys(collection)
            .loads()
            .loadsMissed();
        loader = loader.expiration(gc.calculateExpiration());
        if (elementType == List.class) {
          loader = loader.writeAsList();
          defaultValue = Collections.emptyList();
        } else if (elementType == Set.class) {
          loader = loader.writeAsSet();
          defaultValue = Collections.emptySet();
        } else if (elementType == Map.class) {
          loader = loader.writeAsMap();
          defaultValue = Collections.emptyMap();
        } else {
          if (gc.wrapper) {
            loader = loader.writeAsWrapper();
          } else {
            loader = loader.write();
          }
        }
        if (defaultValue == null) {
          return loader.getAndResortResult();
        } else {
          return loader.getAndResortResult(defaultValue);
        }
      } catch (InternalInvocationTargetException ex) {
        throw ex.getCause();
      }
    }
  }

  private void onCacheHit(String key) {
    if (target instanceof CacheHitListener) {
      CacheHitListener cacheHitListener = (CacheHitListener) target;
      cacheHitListener.failSafeOnCacheHit(key);
    }
  }

  private void onCacheMiss(String key) {
    if (target instanceof CacheMissListener) {
      CacheMissListener cacheMissListener = (CacheMissListener) target;
      cacheMissListener.failSafeOnCacheMiss(key);
    }
  }

  private String generateSingleCacheKey(GoblinCacheMethod cacheMethod,
                                        Object[] arguments) {
    List<String> keys = new ArrayList<>();
    List<Object> values = new ArrayList<>();
    for (GoblinCacheMethodParameter pmi : cacheMethod.parameterList) {
      keys.add(pmi.name);
      values.add(arguments[pmi.index]);
    }
    return CacheKeyGenerator.generateCacheKey(
        cacheMethod.type,
        keys.toArray(new String[0]),
        values.toArray(new Object[0])
    );
  }
}
