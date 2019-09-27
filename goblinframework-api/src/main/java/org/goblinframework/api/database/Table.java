package org.goblinframework.api.database;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  String table();

  boolean dynamic() default false;
}
