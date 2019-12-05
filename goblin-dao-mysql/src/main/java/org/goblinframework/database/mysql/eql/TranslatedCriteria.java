package org.goblinframework.database.mysql.eql;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Translated criteria data structure.
 */
public class TranslatedCriteria {

  public String sql;
  public MapSqlParameterSource parameterSource;

  public TranslatedCriteria() {
  }

  public TranslatedCriteria(String sql, MapSqlParameterSource parameterSource) {
    this.sql = sql;
    this.parameterSource = parameterSource;
  }
}
