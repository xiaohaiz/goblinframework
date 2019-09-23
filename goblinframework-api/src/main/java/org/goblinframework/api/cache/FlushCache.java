package org.goblinframework.api.cache;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCache {

  CacheSystem system();

  String name() default "";

}
