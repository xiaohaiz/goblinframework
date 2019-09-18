package org.goblinframework.dao.mysql.client;

import com.zaxxer.hikari.HikariDataSource;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.dao.mysql.module.config.DataSourceConfig;
import org.jetbrains.annotations.NotNull;

@GoblinManagedBean(type = "DAO.MYSQL")
final class DataSource extends GoblinManagedObject implements DataSourceMXBean {

  private final DataSourceConfig config;
  private final HikariDataSource hikariDataSource;

  DataSource(@NotNull DataSourceConfig config,
             @NotNull HikariDataSource hikariDataSource) {
    this.config = config;
    this.hikariDataSource = hikariDataSource;
  }

  void destroy() {
    unregisterIfNecessary();
    hikariDataSource.close();
  }
}
