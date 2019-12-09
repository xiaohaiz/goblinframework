package org.goblinframework.dao.mongo.persistence;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoPersistenceDatabase {

  String database();

  boolean dynamic() default false;

}
