package org.goblinframework.cache.core;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheParameter {

  String value() default "";

  boolean multiple() default false;

}
