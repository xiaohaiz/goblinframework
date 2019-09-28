package org.goblinframework.cache.core.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheBeans {

  GoblinCacheBean[] value();

}
