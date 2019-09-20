package org.goblinframework.cache.core.annotation;

public enum CachedExpirationPolicy {

  THIS_MONTH, // 截止到本月的最后一秒
  THIS_WEEK,  // 截止到本周的最后一秒
  TODAY,      // 截止到今天的最后一秒
  FIXED       // 固定的秒数，需要自行指定

}
