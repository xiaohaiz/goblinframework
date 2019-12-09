package org.goblinframework.dao.mysql.client;

import org.goblinframework.database.mysql.module.monitor.MSQ;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;

public class MysqlDataSourceTransactionManager extends DataSourceTransactionManager {

  public MysqlDataSourceTransactionManager(DataSource dataSource) {
    super(dataSource);
  }


  @Override
  protected void doBegin(Object transaction, TransactionDefinition definition) {
    try (MSQ instruction = new MSQ()) {
      instruction.operation = "beginTransaction";
      super.doBegin(transaction, definition);
    }
  }

  @Override
  protected void doRollback(DefaultTransactionStatus status) {
    try (MSQ instruction = new MSQ()) {
      instruction.operation = "rollbackTransaction";
      super.doRollback(status);
    }
  }

  @Override
  protected void doCommit(DefaultTransactionStatus status) {
    try (MSQ instruction = new MSQ()) {
      instruction.operation = "commitTransaction";
      super.doCommit(status);
    }
  }
}
