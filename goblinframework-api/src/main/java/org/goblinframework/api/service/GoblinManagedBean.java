package org.goblinframework.api.service;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinManagedBean {

  String type() default "";

  String name() default "";

  boolean register() default true;

}
