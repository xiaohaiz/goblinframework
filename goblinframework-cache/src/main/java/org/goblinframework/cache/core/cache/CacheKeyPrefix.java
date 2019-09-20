package org.goblinframework.cache.core.cache;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheKeyPrefix {

  String prefix();

}
