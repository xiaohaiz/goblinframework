package org.goblinframework.dao.mysql.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class MysqlDataSourceTransactionManager extends DataSourceTransactionManager {

  public MysqlDataSourceTransactionManager(DataSource dataSource) {
    super(dataSource);
  }

}
