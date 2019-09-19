package org.goblinframework.dao.mysql.support;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Collection;

abstract public class MysqlPersistenceSupport<E, ID> extends MysqlPrimaryKeySupport<E, ID> {

  public void $inserts(@NotNull Collection<E> entities) {
    if (entities.isEmpty()) return;
    long millis = System.currentTimeMillis();
    for (E entity : entities) {
      generateEntityId(entity);
      requireEntityId(entity);
      touchCreateTime(entity, millis);
      touchUpdateTime(entity, millis);
      initializeRevision(entity);
    }
    if (entities.size() == 1) {
      internalInserts(entities);
    } else {
      client.getMasterTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          internalInserts(entities);
        }
      });
    }
  }

  private void internalInserts(Collection<E> entities) {
    groupEntities(entities).forEach((tableName, list) -> {

    });
  }
}
