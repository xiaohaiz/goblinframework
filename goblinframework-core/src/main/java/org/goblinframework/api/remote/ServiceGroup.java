package org.goblinframework.api.remote;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceGroup {

  String group() default "";

  boolean enable() default true;

}
