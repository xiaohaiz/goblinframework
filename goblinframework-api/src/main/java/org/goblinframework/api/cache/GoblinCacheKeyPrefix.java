package org.goblinframework.api.cache;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GoblinCacheKeyPrefix {

  String prefix();

}
