package org.goblinframework.dao.mysql.client;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.dao.mysql.module.config.DataSourceConfig;
import org.goblinframework.dao.mysql.module.config.MysqlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@GoblinManagedBean(type = "DAO.MYSQL")
final public class MysqlClient extends GoblinManagedObject implements MysqlClientMXBean {

  private final DataSource master;
  private final List<DataSource> slaves = new LinkedList<>();

  public MysqlClient(@NotNull MysqlConfig config) {
    this.master = DataSourceBuilder.buildDataSource((DataSourceConfig) config.getMaster());
    Arrays.stream(config.getSlaveList())
        .map(e -> (DataSourceConfig) e)
        .map(DataSourceBuilder::buildDataSource)
        .forEach(slaves::add);
  }

  @NotNull
  public javax.sql.DataSource getMasterDataSource() {
    return master.getDataSource();
  }

  void destroy() {
    unregisterIfNecessary();
    master.destroy();
    slaves.forEach(DataSource::destroy);
  }

  @NotNull
  @Override
  public DataSourceMXBean getMaster() {
    return master;
  }
}
