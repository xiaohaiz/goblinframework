package org.goblinframework.api.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceVersion {

  String version() default "1.0";

  boolean enable() default true;

}
