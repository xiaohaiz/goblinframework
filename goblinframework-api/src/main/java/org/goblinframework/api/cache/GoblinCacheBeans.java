package org.goblinframework.api.cache;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheBeans {

  GoblinCacheBean[] value();

}
