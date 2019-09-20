package org.goblinframework.dao.mysql.client;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public class MasterMysqlConnection {

  private final DataSource dataSource;
  private final PlatformTransactionManager transactionManager;
  private final TransactionTemplate transactionTemplate;
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public MasterMysqlConnection(@NotNull DataSource dataSource) {
    this.dataSource = dataSource;
    this.transactionManager = new MysqlDataSourceTransactionManager(dataSource);
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public PlatformTransactionManager getTransactionManager() {
    return transactionManager;
  }

  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }
}
