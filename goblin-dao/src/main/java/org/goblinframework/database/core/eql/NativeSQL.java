package org.goblinframework.database.core.eql;

import java.util.Map;

public class NativeSQL {
  private String sql;
  private Map<String, Object> parameters;

  public NativeSQL() {
  }

  public NativeSQL(String sql, Map<String, Object> parameters) {
    this.sql = sql;
    this.parameters = parameters;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
}
