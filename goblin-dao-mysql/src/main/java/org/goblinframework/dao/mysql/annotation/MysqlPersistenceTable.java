package org.goblinframework.dao.mysql.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MysqlPersistenceTable {

  String table();

  boolean dynamic() default false;
}
