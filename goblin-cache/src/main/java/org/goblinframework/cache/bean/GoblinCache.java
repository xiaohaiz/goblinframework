package org.goblinframework.cache.bean;

import org.goblinframework.cache.annotation.CacheExpiration;
import org.goblinframework.cache.core.Cache;
import org.goblinframework.cache.core.CacheExpirationCalculator;
import org.goblinframework.cache.core.CacheLocation;

public class GoblinCache {

  public Class<?> type;
  public CacheLocation location;
  public boolean wrapper;
  public CacheExpiration.Policy expirationPolicy = CacheExpiration.Policy.FIXED;
  public int expirationValue = CacheExpiration.DEFAULT_EXPIRATION;

  public Cache cache;

  public int calculateExpiration() {
    CacheExpiration annotation = type.getAnnotation(CacheExpiration.class);
    if (annotation != null && annotation.enable()) {
      return CacheExpirationCalculator.expirationInSeconds(annotation);
    }
    return CacheExpirationCalculator.expirationInSeconds(expirationPolicy, expirationValue);
  }

  public Cache cache() {
    return cache;
  }
}
