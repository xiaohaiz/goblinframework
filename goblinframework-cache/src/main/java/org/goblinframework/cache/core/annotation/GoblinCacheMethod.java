package org.goblinframework.cache.core.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheMethod {

  Class<?> value();

}
