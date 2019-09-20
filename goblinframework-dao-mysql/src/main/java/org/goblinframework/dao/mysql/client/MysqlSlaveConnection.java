package org.goblinframework.dao.mysql.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public class MysqlSlaveConnection extends MysqlConnection {

  public MysqlSlaveConnection(@NotNull DataSource dataSource) {
    super(dataSource);
  }

  @NotNull
  @Override
  public PlatformTransactionManager getTransactionManager() {
    throw new UnsupportedOperationException("MYSQL slave connection has not transaction supported");
  }

  @NotNull
  @Override
  public TransactionTemplate getTransactionTemplate() {
    throw new UnsupportedOperationException("MYSQL slave connection has not transaction supported");
  }

  @Nullable
  @Override
  public <E> E executeTransaction(@NotNull TransactionCallback<E> action) {
    throw new UnsupportedOperationException("MYSQL slave connection has not transaction supported");
  }

  @Override
  public void executeTransactionWithoutResult(@NotNull TransactionCallbackWithoutResult action) {
    throw new UnsupportedOperationException("MYSQL slave connection has not transaction supported");
  }
}
