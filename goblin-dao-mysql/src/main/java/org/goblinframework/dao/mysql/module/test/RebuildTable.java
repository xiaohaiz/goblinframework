package org.goblinframework.dao.mysql.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RebuildTable {

  String connection();

  String table();

  RebuildTableScope scope() default @RebuildTableScope(enable = false);

}
