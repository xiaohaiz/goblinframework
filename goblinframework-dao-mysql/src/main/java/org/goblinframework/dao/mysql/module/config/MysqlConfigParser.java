package org.goblinframework.dao.mysql.module.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.goblinframework.core.config.BufferedConfigParser;
import org.goblinframework.core.config.ConfigLoader;
import org.goblinframework.core.config.ConfigMapping;
import org.goblinframework.core.mapper.JsonMapper;

import java.util.*;
import java.util.stream.Collectors;

final class MysqlConfigParser extends BufferedConfigParser<MysqlConfig> {

  @Override
  public void initialize() {
    ConfigMapping mapping = ConfigLoader.INSTANCE.getMapping();
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
    return new MysqlConfig(name, new DataSourceConfig(master),
        slaves.stream().map(DataSourceConfig::new).collect(Collectors.toList()));
  }
}
