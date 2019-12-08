package org.goblinframework.dao.mysql.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RebuildTableScope {

  boolean enable() default true;

  RebuildTableParameter[] parameters() default {};

  int from() default 0;

  int to() default 0;

}
