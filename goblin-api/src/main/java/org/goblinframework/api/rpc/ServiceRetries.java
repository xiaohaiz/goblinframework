package org.goblinframework.api.rpc;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceRetries {

  int retries() default 0;

  boolean enable() default true;

}
