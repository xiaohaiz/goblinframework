package org.goblinframework.dao.mysql.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RebuildMysqlTable {

  String name();

  Class<?> entity() default Void.class;

  String table() default "";

  boolean range() default true;

  int from() default 0;

  int to() default 0;

  String[] arguments() default {};

}
