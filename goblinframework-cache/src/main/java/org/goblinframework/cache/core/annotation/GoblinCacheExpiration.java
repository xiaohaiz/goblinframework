package org.goblinframework.cache.core.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheExpiration {

  int DEFAULT_EXPIRATION = 3600;

  int value() default DEFAULT_EXPIRATION;

  CachedExpirationPolicy policy() default CachedExpirationPolicy.FIXED;

  boolean enable() default true;
}
