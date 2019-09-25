package org.goblinframework.api.dao;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

  Generator value();

  enum Generator {
    NONE,
    OBJECT_ID,
    AUTO_INC
  }
}
