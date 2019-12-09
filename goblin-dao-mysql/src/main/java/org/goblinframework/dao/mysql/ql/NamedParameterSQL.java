package org.goblinframework.dao.mysql.ql;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * SQL and named parameters.
 */
public class NamedParameterSQL {

  public String sql;
  public MapSqlParameterSource source;

  public NamedParameterSQL() {
  }

  public NamedParameterSQL(String sql, MapSqlParameterSource source) {
    this.sql = sql;
    this.source = source;
  }
}
