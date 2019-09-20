package org.goblinframework.cache.core.cache;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheKeyRevision {

  String revision();

}
