package org.goblinframework.cache.core.annotation;

import org.goblinframework.core.cache.GoblinCacheSystem;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheBean {

  Class<?> type();

  GoblinCacheSystem system() default GoblinCacheSystem.CBS;

  String name();

  boolean wrapper() default false;

  GoblinCacheExpiration expiration() default @GoblinCacheExpiration(enable = false);

  boolean enable() default true;

}
