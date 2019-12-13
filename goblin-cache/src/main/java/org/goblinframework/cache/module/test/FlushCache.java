package org.goblinframework.cache.module.test;

import org.goblinframework.cache.core.cache.CacheSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCache {

  CacheSystem system();

  String connection();

}
