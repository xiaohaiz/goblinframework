package org.goblinframework.cache.core.support;

import org.goblinframework.cache.core.annotation.GoblinCacheExpiration;
import org.goblinframework.cache.core.cache.CacheSystemLocation;
import org.goblinframework.cache.core.util.CacheExpirationCalculator;

public class GoblinCache {

  public Class<?> type;
  public CacheSystemLocation location;
  public boolean wrapper;
  public GoblinCacheExpiration.Policy expirationPolicy = GoblinCacheExpiration.Policy.FIXED;
  public int expirationValue = GoblinCacheExpiration.DEFAULT_EXPIRATION;

  public org.goblinframework.cache.core.cache.GoblinCache cache;

  public int calculateExpiration() {
    GoblinCacheExpiration annotation = type.getAnnotation(GoblinCacheExpiration.class);
    if (annotation != null && annotation.enable()) {
      return CacheExpirationCalculator.expirationInSeconds(annotation);
    }
    return CacheExpirationCalculator.expirationInSeconds(expirationPolicy, expirationValue);
  }

  public org.goblinframework.cache.core.cache.GoblinCache cache() {
    return cache;
  }
}
