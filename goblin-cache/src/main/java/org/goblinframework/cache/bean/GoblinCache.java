package org.goblinframework.cache.bean;

import org.goblinframework.cache.annotation.GoblinCacheExpiration;
import org.goblinframework.cache.core.cache.Cache;
import org.goblinframework.cache.core.cache.CacheLocation;
import org.goblinframework.cache.util.CacheExpirationCalculator;

public class GoblinCache {

  public Class<?> type;
  public CacheLocation location;
  public boolean wrapper;
  public GoblinCacheExpiration.Policy expirationPolicy = GoblinCacheExpiration.Policy.FIXED;
  public int expirationValue = GoblinCacheExpiration.DEFAULT_EXPIRATION;

  public Cache cache;

  public int calculateExpiration() {
    GoblinCacheExpiration annotation = type.getAnnotation(GoblinCacheExpiration.class);
    if (annotation != null && annotation.enable()) {
      return CacheExpirationCalculator.expirationInSeconds(annotation);
    }
    return CacheExpirationCalculator.expirationInSeconds(expirationPolicy, expirationValue);
  }

  public Cache cache() {
    return cache;
  }
}
