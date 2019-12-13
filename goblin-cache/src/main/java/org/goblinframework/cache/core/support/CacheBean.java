package org.goblinframework.cache.core.support;

import org.goblinframework.cache.core.annotation.GoblinCacheBean;
import org.goblinframework.cache.core.annotation.GoblinCacheExpiration;
import org.goblinframework.cache.core.cache.Cache;
import org.goblinframework.cache.core.cache.CacheLocation;
import org.goblinframework.cache.core.cache.CacheSystem;
import org.goblinframework.cache.exception.GoblinCacheException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheBean {

  private final IdentityHashMap<Class<?>, GoblinCache> caches = new IdentityHashMap<>();
  final Map<Method, CacheMethod> methods = new LinkedHashMap<>();

  public CacheBean() {
  }

  public CacheBean(@NotNull List<GoblinCacheBean> annotations) {
    for (GoblinCacheBean annotation : annotations) {
      GoblinCache gc = new GoblinCache();
      gc.type = annotation.type();
      gc.location = new CacheLocation(annotation.system(), annotation.name());
      gc.wrapper = annotation.wrapper();
      GoblinCacheExpiration expiration = annotation.expiration();
      if (expiration.enable()) {
        gc.expirationPolicy = expiration.policy();
        gc.expirationValue = expiration.value();
      }
      CacheSystem cacheSystem = gc.location.system;
      String cacheName = gc.location.name;
      Cache cache = cacheSystem.cache(cacheName);
      if (cache == null) {
        String errMsg = "GOBLIN cache [%s/%s] not available";
        errMsg = String.format(errMsg, cacheSystem, cacheName);
        throw new GoblinCacheException(errMsg);
      }
      gc.cache = cache;
      caches.put(gc.type, gc);
    }
  }

  public boolean isEmpty() {
    return caches.isEmpty();
  }

  @Nullable
  public GoblinCache getGoblinCache(@NotNull Class<?> type) {
    return caches.get(type);
  }

  public CacheMethod getGoblinCacheMethod(Method method) {
    return methods.get(method);
  }
}
