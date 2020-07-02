package org.goblinframework.api.rpc;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeService {

  Class<?> interfaceClass();

  ServiceVersion version() default @ServiceVersion(enable = false);

  boolean enable() default true;

}
