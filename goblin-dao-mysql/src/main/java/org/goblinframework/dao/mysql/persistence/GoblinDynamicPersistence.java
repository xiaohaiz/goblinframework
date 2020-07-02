package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.goblinframework.dao.mysql.persistence.internal.MysqlPersistenceOperationSupport;

abstract public class GoblinDynamicPersistence<E, ID> extends MysqlPersistenceOperationSupport<E, ID> {

  protected GoblinDynamicPersistence() {
    if (!isDynamicTable()) {
      throw new GoblinMysqlPersistenceException("Dynamic table is required");
    }
  }
}
