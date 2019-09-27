package org.goblinframework.dao.mysql.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

abstract public class MysqlConnection {

  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public MysqlConnection(@NotNull DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  @NotNull
  public DataSource getDataSource() {
    return dataSource;
  }

  @NotNull
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @NotNull
  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }

  @NotNull
  abstract public PlatformTransactionManager getTransactionManager();

  @NotNull
  abstract public TransactionTemplate getTransactionTemplate();

  @Nullable
  abstract public <E> E executeTransaction(@NotNull TransactionCallback<E> action);

  abstract public void executeTransactionWithoutResult(@NotNull TransactionCallbackWithoutResult action);

}
