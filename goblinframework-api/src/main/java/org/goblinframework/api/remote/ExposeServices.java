package org.goblinframework.api.remote;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeServices {

  ExposeService[] value();

  boolean enable() default true;

}
