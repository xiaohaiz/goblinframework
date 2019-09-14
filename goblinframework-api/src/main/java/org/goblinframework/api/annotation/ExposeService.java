package org.goblinframework.api.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeService {

  Class<?> interfaceClass();

  ServiceGroup group() default @ServiceGroup(enable = false);

  ServiceVersion version() default @ServiceVersion(enable = false);

  boolean enable() default true;

}
