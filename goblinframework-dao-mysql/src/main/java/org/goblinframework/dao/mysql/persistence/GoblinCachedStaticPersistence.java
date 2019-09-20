package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.mysql.support.MysqlCachedPersistenceSupport;
import org.jetbrains.annotations.NotNull;

abstract public class GoblinCachedStaticPersistence<E, ID> extends MysqlCachedPersistenceSupport<E, ID> {

  @NotNull
  @Override
  protected String calculateTableName(@NotNull String template, @NotNull E document) {
    return getEntityTableName(null);
  }

}
