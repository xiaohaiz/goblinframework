package org.goblinframework.api.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateTime {

  String pattern() default "yyyy-MM-dd HH:mm:ss.SSS";

}
