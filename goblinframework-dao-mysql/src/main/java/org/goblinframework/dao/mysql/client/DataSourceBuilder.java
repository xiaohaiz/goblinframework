package org.goblinframework.dao.mysql.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.goblinframework.dao.mysql.module.config.DataSourceConfig;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

abstract class DataSourceBuilder {

  @NotNull
  static DataSource buildDataSource(@NotNull DataSourceConfig config) {
    HikariConfig hikariConfig = new HikariConfig();
    if (config.getDataSourceClassName() != null && config.getDatabaseName() != null) {
      hikariConfig.setDataSourceClassName(config.getDataSourceClassName());
      hikariConfig.addDataSourceProperty("databaseName", config.getDatabaseName());
    } else {
      hikariConfig.setDriverClassName(config.getDriverClass());
      hikariConfig.setJdbcUrl(config.getJdbcUrl());
    }
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getMapper().getPassword());

    // Frequently used
    hikariConfig.setAutoCommit(config.getAutoCommit());
    hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
    hikariConfig.setIdleTimeout(config.getIdleTimeout());
    hikariConfig.setMaxLifetime(config.getMaxLifetime());
    if (config.getConnectionTestQuery() != null)
      hikariConfig.setConnectionTestQuery(config.getConnectionTestQuery());
    hikariConfig.setMinimumIdle(config.getMinimumIdle());
    hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
    if (config.getPoolName() != null)
      hikariConfig.setPoolName(config.getPoolName());

    // Infrequently used
    hikariConfig.setIsolateInternalQueries(config.getIsolateInternalQueries());
    hikariConfig.setAllowPoolSuspension(config.getAllowPoolSuspension());
    hikariConfig.setReadOnly(config.getReadOnly());
    hikariConfig.setRegisterMbeans(config.getRegisterMbeans());
    if (config.getCatalog() != null)
      hikariConfig.setCatalog(config.getCatalog());
    if (config.getConnectionInitSql() != null)
      hikariConfig.setConnectionInitSql(config.getConnectionInitSql());
    if (config.getTransactionIsolation() != null)
      hikariConfig.setTransactionIsolation(config.getTransactionIsolation());
    hikariConfig.setValidationTimeout(config.getValidationTimeout());
    hikariConfig.setLeakDetectionThreshold(config.getLeakDetectionThreshold());

    // Properties
    LinkedHashMap<String, String> dataSourceProperties = config.getMapper().getDataSourceProperties();
    if (dataSourceProperties != null && !dataSourceProperties.isEmpty()) {
      dataSourceProperties.forEach(hikariConfig::addDataSourceProperty);
    }

    HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
    return new DataSource(config, hikariDataSource);
  }
}
