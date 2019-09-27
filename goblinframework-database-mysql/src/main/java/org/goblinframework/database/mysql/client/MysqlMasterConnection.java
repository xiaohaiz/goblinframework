package org.goblinframework.database.mysql.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public class MysqlMasterConnection extends MysqlConnection {

  private final PlatformTransactionManager transactionManager;
  private final TransactionTemplate transactionTemplate;

  public MysqlMasterConnection(@NotNull DataSource dataSource) {
    super(dataSource);
    this.transactionManager = new MysqlDataSourceTransactionManager(dataSource);
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }

  @NotNull
  @Override
  public PlatformTransactionManager getTransactionManager() {
    return transactionManager;
  }

  @NotNull
  @Override
  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  @Nullable
  @Override
  public <E> E executeTransaction(@NotNull TransactionCallback<E> action) {
    return transactionTemplate.execute(action);
  }

  @Override
  public void executeTransactionWithoutResult(@NotNull TransactionCallbackWithoutResult action) {
    transactionTemplate.execute(action);
  }
}
