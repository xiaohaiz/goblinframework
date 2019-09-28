package org.goblinframework.api.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal {

  boolean installRequired() default false;

  boolean uniqueInstance() default false;

}
