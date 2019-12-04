package org.goblinframework.api.remote;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceTimeout {

  int timeout();

  TimeUnit unit();

  boolean enable() default true;

}
