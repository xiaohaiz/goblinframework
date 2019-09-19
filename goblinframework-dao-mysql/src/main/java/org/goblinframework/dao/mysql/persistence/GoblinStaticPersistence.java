package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.mysql.support.MysqlPersistenceSupport;
import org.jetbrains.annotations.NotNull;

abstract public class GoblinStaticPersistence<E, ID> extends MysqlPersistenceSupport<E, ID> {

  @NotNull
  @Override
  protected String calculateTableName(@NotNull String template, @NotNull E document) {
    return getEntityTableName(null);
  }
}
