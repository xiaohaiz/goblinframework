package org.goblinframework.api.rpc;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoResponseWait {

  boolean value() default true;

  boolean dispatchAll() default false;

  boolean ignoreNoProvider() default false;

  boolean enable() default true;

}
