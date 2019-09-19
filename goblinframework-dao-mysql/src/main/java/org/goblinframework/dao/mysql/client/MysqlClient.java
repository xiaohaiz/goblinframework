package org.goblinframework.dao.mysql.client;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.dao.mysql.module.config.DataSourceConfig;
import org.goblinframework.dao.mysql.module.config.MysqlConfig;
import org.goblinframework.dao.mysql.transaction.MysqlDataSourceTransactionManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@GoblinManagedBean(type = "DAO.MYSQL")
final public class MysqlClient extends GoblinManagedObject implements MysqlClientMXBean {

  private final DataSource master;
  private final List<DataSource> slaves = new LinkedList<>();

  private final MysqlDataSourceTransactionManager masterTransactionManager;
  private final TransactionTemplate masterTransactionTemplate;
  private final JdbcTemplate masterJdbcTemplate;
  private final NamedParameterJdbcTemplate masterNamedParameterJdbcTemplate;

  public MysqlClient(@NotNull MysqlConfig config) {
    this.master = DataSourceBuilder.buildDataSource((DataSourceConfig) config.getMaster());
    Arrays.stream(config.getSlaveList())
        .map(e -> (DataSourceConfig) e)
        .map(DataSourceBuilder::buildDataSource)
        .forEach(slaves::add);

    this.masterTransactionManager = new MysqlDataSourceTransactionManager(getMasterDataSource());
    this.masterTransactionTemplate = new TransactionTemplate(masterTransactionManager);
    this.masterJdbcTemplate = new JdbcTemplate(getMasterDataSource());
    this.masterNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(masterJdbcTemplate);
  }

  public MysqlDataSourceTransactionManager getMasterTransactionManager() {
    return masterTransactionManager;
  }

  public TransactionTemplate getMasterTransactionTemplate() {
    return masterTransactionTemplate;
  }

  public JdbcTemplate getMasterJdbcTemplate() {
    return masterJdbcTemplate;
  }

  public NamedParameterJdbcTemplate getMasterNamedParameterJdbcTemplate() {
    return masterNamedParameterJdbcTemplate;
  }

  @NotNull
  public javax.sql.DataSource getMasterDataSource() {
    return master.getDataSource();
  }

  @Override
  protected void disposeBean() {
    master.dispose();
    slaves.forEach(DataSource::dispose);
  }

  @NotNull
  @Override
  public DataSourceMXBean getMaster() {
    return master;
  }
}
