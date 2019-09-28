package org.goblinframework.cache.core;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheKeyRevision {

  String revision();

}
