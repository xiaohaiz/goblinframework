package org.goblinframework.api.cache;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheMethod {

  Class<?> value();

}
