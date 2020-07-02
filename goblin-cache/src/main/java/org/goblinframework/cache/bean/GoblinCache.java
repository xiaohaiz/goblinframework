package org.goblinframework.cache.bean;

import org.goblinframework.cache.annotation.CacheExpiration;
import org.goblinframework.cache.core.Cache;
import org.goblinframework.cache.core.CacheLocation;
import org.goblinframework.cache.core._CacheExpirationCalculatorKt;
import org.jetbrains.annotations.NotNull;

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
      return _CacheExpirationCalculatorKt.calculateExpirationInSeconds(annotation);
    }
    return _CacheExpirationCalculatorKt.calculateExpirationInSeconds(expirationPolicy, expirationValue);
  }

  @NotNull
  public Cache cache() {
    assert cache != null;
    return cache;
  }
}
