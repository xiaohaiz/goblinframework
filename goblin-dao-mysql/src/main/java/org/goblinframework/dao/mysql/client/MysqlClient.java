package org.goblinframework.dao.mysql.client;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.database.mysql.module.config.DataSourceConfig;
import org.goblinframework.database.mysql.module.config.MysqlConfig;
import org.goblinframework.database.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@GoblinManagedBean(type = "MysqlDao")
final public class MysqlClient extends GoblinManagedObject implements MysqlClientMXBean {

  private final AtomicLong counter = new AtomicLong(0);
  private final MysqlConfig config;
  private final DataSource master;
  private final List<DataSource> slaves = new LinkedList<>();
  private final MysqlMasterConnection masterConnection;

  public MysqlClient(@NotNull MysqlConfig config) {
    this.config = config;
    this.master = DataSourceBuilder.buildDataSource(config.getName(), "MASTER", (DataSourceConfig) config.getMaster());
    Arrays.stream(config.getSlaveList())
        .map(e -> (DataSourceConfig) e)
        .map(e -> DataSourceBuilder.buildDataSource(config.getName(), "SLAVE", e))
        .forEach(slaves::add);
    this.masterConnection = new MysqlMasterConnection(this.master.getDataSource());
  }

  @NotNull
  public MysqlMasterConnection getMasterConnection() {
    return masterConnection;
  }

  @NotNull
  public MysqlSlaveConnection createSlaveConnection() {
    MysqlSlaveConnection connection = createSlaveConnection(true);
    assert connection != null;
    return connection;
  }

  @Nullable
  public MysqlSlaveConnection createSlaveConnection(boolean failOnNoSlave) {
    javax.sql.DataSource dataSource = getSlaveDataSource();
    if (dataSource == null) {
      if (failOnNoSlave) {
        String errMsg = "MYSQL client [%s] No slave(s) configured";
        errMsg = String.format(errMsg, config.getName());
        throw new GoblinPersistenceException(errMsg);
      } else {
        return null;
      }
    } else {
      return new MysqlSlaveConnection(dataSource);
    }
  }

  @NotNull
  public javax.sql.DataSource getMasterDataSource() {
    return master.getDataSource();
  }

  @Nullable
  public javax.sql.DataSource getSlaveDataSource() {
    if (slaves.isEmpty()) return null;
    if (slaves.size() == 1) return slaves.get(0).getDataSource();
    int idx = (int) (counter.getAndIncrement() % slaves.size());
    return slaves.get(idx).getDataSource();
  }

  @Override
  protected void disposeBean() {
    master.dispose();
    slaves.forEach(DataSource::dispose);
  }

  @NotNull
  @Override
  public String getName() {
    return config.getName();
  }

  @NotNull
  @Override
  public DataSourceMXBean getMaster() {
    return master;
  }
}
