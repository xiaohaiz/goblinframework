package org.goblinframework.api.remote;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceVersion {

  String DEFAULT_VERSION = "1.0.0";

  String version() default DEFAULT_VERSION;

  boolean enable() default true;

}
