package org.goblinframework.dao.mysql.client;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class MysqlSlaveConnection {

  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public MysqlSlaveConnection(@NotNull DataSource dataSource) {
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

}
