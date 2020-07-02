package org.goblinframework.dao.mongo.module.test;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDatabases {
  DropDatabase[] value();
}
