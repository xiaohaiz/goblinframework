package org.goblinframework.dao.mysql.support;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseMysqlClient {
  String value();
}
