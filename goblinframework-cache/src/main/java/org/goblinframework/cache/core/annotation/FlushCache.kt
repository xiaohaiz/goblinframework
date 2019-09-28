package org.goblinframework.cache.core.annotation;

import org.goblinframework.cache.core.CacheSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCache {

  CacheSystem system();

  String name();

}
