package org.goblinframework.cache.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCaches {

  FlushCache[] value();

}
