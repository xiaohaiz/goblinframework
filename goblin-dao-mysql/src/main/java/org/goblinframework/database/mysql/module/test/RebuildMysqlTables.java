package org.goblinframework.database.mysql.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RebuildMysqlTables {

  RebuildMysqlTable[] value();

}
