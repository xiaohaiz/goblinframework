package org.goblinframework.api.cache;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheParameter {

  String value() default "";

  boolean multiple() default false;

}
