package org.goblinframework.cache.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheParameter {

  String value() default "";

  boolean multiple() default false;

}
