package org.goblinframework.dao.mysql.module.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.goblinframework.core.config.BufferedConfigParser;
import org.goblinframework.core.config.ConfigManager;
import org.goblinframework.core.config.ConfigMapping;
import org.goblinframework.core.config.GoblinConfigException;
import org.goblinframework.core.mapper.JsonMapper;

import java.util.*;
import java.util.stream.Collectors;

final public class MysqlConfigParser extends BufferedConfigParser<MysqlConfig> {

  @Override
  public void initializeBean() {
    ConfigMapping mapping = ConfigManager.INSTANCE.getMapping();
    Object mysql = mapping.get("mysql");
    if (!(mysql instanceof Map)) {
      return;
    }
    Map m = (Map) mysql;
    for (Object o : m.entrySet()) {
      Map.Entry e = (Map.Entry) o;
      String name = (String) e.getKey();
      Map value = (Map) e.getValue();
      MysqlConfig config = createMysqlConfig(name, value);
      putIntoBuffer(name, config);
    }
  }

  private MysqlConfig createMysqlConfig(String name, Map value) {
    ObjectMapper mapper = JsonMapper.getDefaultObjectMapper();
    DataSourceConfigMapper master = mapper.convertValue(value.get("master"), DataSourceConfigMapper.class);
    List<DataSourceConfigMapper> slaves = Collections.emptyList();
    Object s = value.get("slaves");
    if (s instanceof Collection) {
      JavaType jt = mapper.getTypeFactory().constructCollectionType(LinkedList.class, DataSourceConfigMapper.class);
      slaves = mapper.convertValue(s, jt);
    }
    return new MysqlConfig(name, processDataSourceConfig(new DataSourceConfig(master)),
        slaves.stream().map(e -> {
          DataSourceConfig config = processDataSourceConfig(new DataSourceConfig(e));
          config.getMapper().setReadOnly(true);
          return config;
        }).collect(Collectors.toList()));
  }

  private DataSourceConfig processDataSourceConfig(DataSourceConfig config) {
    DataSourceConfigMapper mapper = config.getMapper();

    if (mapper.getDriverClass() == null) mapper.setDriverClass("com.mysql.jdbc.Driver");
    if (mapper.getAutoCommit() == null) mapper.setAutoCommit(true);
    if (mapper.getConnectionTimeout() == null) mapper.setConnectionTimeout(30000L);
    if (mapper.getIdleTimeout() == null) mapper.setIdleTimeout(600000L);
    if (mapper.getMaxLifetime() == null) mapper.setMaxLifetime(1800000L);
    if (mapper.getMaximumPoolSize() == null) mapper.setMaximumPoolSize(10);
    if (mapper.getMinimumIdle() == null) mapper.setMinimumIdle(1);
    if (mapper.getIsolateInternalQueries() == null) mapper.setIsolateInternalQueries(false);
    if (mapper.getAllowPoolSuspension() == null) mapper.setAllowPoolSuspension(false);
    if (mapper.getReadOnly() == null) mapper.setReadOnly(false);
    if (mapper.getRegisterMbeans() == null) mapper.setRegisterMbeans(true);
    if (mapper.getValidationTimeout() == null) mapper.setValidationTimeout(5000L);
    if (mapper.getLeakDetectionThreshold() == null) mapper.setLeakDetectionThreshold(0L);

    boolean c1 = config.getDataSourceClassName() != null && config.getDatabaseName() != null;
    boolean c2 = config.getJdbcUrl() != null && config.getDriverClass() != null;
    if (!c1 && !c2) {
      throw new GoblinConfigException("dataSourceClassName/databaseName or jdbcUrl/driverClassName is required");
    }

    return config;
  }
}
