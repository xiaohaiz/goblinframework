package org.goblinframework.dao.mongo.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoPersistenceCollection {

  String collection();

  boolean dynamic() default false;

}
