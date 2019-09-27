package org.goblinframework.database.core;

import java.lang.annotation.*;

/**
 * Define which database should be connected. This annotation
 * should be presented on persistence/dao implementation class.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinDatabaseConnection {

  /**
   * The name of database config which to be connected.
   */
  String name();

}
