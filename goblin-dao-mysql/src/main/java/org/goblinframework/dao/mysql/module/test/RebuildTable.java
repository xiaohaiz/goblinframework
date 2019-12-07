package org.goblinframework.dao.mysql.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RebuildTable {

  String connection();

  String table();

  boolean range() default true;

  int from() default 0;

  int to() default 0;

  String[] arguments() default {};

}
