package org.goblinframework.cache.core.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheExpiration {

  int DEFAULT_EXPIRATION = 3600;

  int value() default DEFAULT_EXPIRATION;

  Policy policy() default Policy.FIXED;

  boolean enable() default true;

  enum Policy {

    THIS_MONTH, // 截止到本月的最后一秒
    THIS_WEEK,  // 截止到本周的最后一秒
    TODAY,      // 截止到今天的最后一秒
    FIXED       // 固定的秒数，需要自行指定

  }
}
