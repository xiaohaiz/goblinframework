package org.goblinframework.cache.annotation;

import org.goblinframework.cache.core.CacheSystem;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheBean {

  Class<?> type();

  CacheSystem system();

  String connection();

  boolean wrapper() default false;

  CacheExpiration expiration() default @CacheExpiration(enable = false);

  boolean enable() default true;

}
