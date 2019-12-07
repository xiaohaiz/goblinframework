package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.mysql.persistence.internal.MysqlPersistenceOperationSupport;

abstract public class GoblinDynamicPersistence<E, ID> extends MysqlPersistenceOperationSupport<E, ID> {

  protected GoblinDynamicPersistence() {
  }
}
