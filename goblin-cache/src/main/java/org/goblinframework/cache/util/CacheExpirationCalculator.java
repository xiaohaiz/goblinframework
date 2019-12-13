package org.goblinframework.cache.util;

import org.goblinframework.cache.core.annotation.GoblinCacheExpiration;
import org.goblinframework.core.util.DateUtils;
import org.jetbrains.annotations.NotNull;

abstract public class CacheExpirationCalculator {

  public static int expirationInSeconds(@NotNull GoblinCacheExpiration annotation) {
    if (!annotation.enable()) {
      throw new IllegalArgumentException("@UtopiaCacheExpiration is not enabled");
    }
    return expirationInSeconds(annotation.policy(), annotation.value());
  }

  public static int expirationInSeconds(@NotNull GoblinCacheExpiration.Policy policy, int value) {
    switch (policy) {
      case FIXED: {
        return Math.max(0, value);
      }
      case TODAY: {
        return DateUtils.getCurrentToDayEndSecond();
      }
      case THIS_WEEK: {
        return DateUtils.getCurrentToWeekEndSecond();
      }
      case THIS_MONTH: {
        return DateUtils.getCurrentToMonthEndSecond();
      }
      default: {
        throw new UnsupportedOperationException();
      }
    }
  }
}
