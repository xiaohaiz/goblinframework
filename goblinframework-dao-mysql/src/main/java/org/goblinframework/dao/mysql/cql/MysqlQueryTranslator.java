package org.goblinframework.dao.mysql.cql;

import org.goblinframework.api.page.Order;
import org.goblinframework.core.conversion.ConversionUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.core.cql.NativeSQL;
import org.goblinframework.dao.core.cql.Query;
import org.goblinframework.dao.core.mapping.EntityMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.*;
import java.util.stream.Collectors;

final public class MysqlQueryTranslator {

  private final EntityMapping mapping;

  public MysqlQueryTranslator(@NotNull EntityMapping mapping) {
    this.mapping = mapping;
  }

  public TranslatedCriteria translateQuery(Query query, String tableName) {
    Objects.requireNonNull(query);

    List<String> list = extractFieldNames(query).stream()
        .map(e -> "`" + e + "`")
        .collect(Collectors.toList());
    String fields = StringUtils.join(list, ",");
    String sql = String.format("SELECT %s FROM `%s`", fields, tableName);
    NativeSQL nativeSQL = query.getNativeSQL();
    if (nativeSQL != null) {
      sql = sql + nativeSQL.getSql();
      MapSqlParameterSource source = new MapSqlParameterSource();
      if (nativeSQL.getParameters() != null) {
        source.addValues(nativeSQL.getParameters());
      }
      return new TranslatedCriteria(sql, source);
    } else {
      TranslatedCriteria tc = MysqlCriteriaTranslator.INSTANCE.translate(query.getCriteria());
      sql = sql + tc.sql;
      sql = sql + extractSort(query);
      sql = sql + extractLimit(query);
      return new TranslatedCriteria(sql, tc.parameterSource);
    }
  }

  public TranslatedCriteria translateCount(Query query, String tableName) {
    return translateCount(query, tableName, false);
  }

  public TranslatedCriteria translateCount(Query query, String tableName, boolean extractLimit) {
    Objects.requireNonNull(query);

    String sql = String.format("SELECT COUNT(1) FROM `%s`", tableName);
    NativeSQL nativeSQL = query.getNativeSQL();
    if (nativeSQL != null) {
      sql = sql + nativeSQL.getSql();
      MapSqlParameterSource source = new MapSqlParameterSource();
      if (nativeSQL.getParameters() != null) {
        source.addValues(nativeSQL.getParameters());
      }
      return new TranslatedCriteria(sql, source);
    } else {
      TranslatedCriteria tc = MysqlCriteriaTranslator.INSTANCE.translate(query.getCriteria());
      sql = sql + tc.sql;
      if (extractLimit) {
        sql = sql + extractLimit(query);
      }
      return new TranslatedCriteria(sql, tc.parameterSource);
    }
  }

  /**
   * Extract all fields which attend querying (ID field always included).
   *
   * @param query query object
   * @return field names.
   */
  private Set<String> extractFieldNames(Query query) {
    Map<String, Boolean> projection = query.field().getProjection();
    Set<String> set = new LinkedHashSet<>(mapping.allFieldNames);
    if (projection.isEmpty()) {
      return set;
    }
    if (projection.values().stream().anyMatch(ConversionUtils::toBoolean)) {
      set.clear();
      set.add(mapping.getIdFieldName());
      String rfn = mapping.getRevisionFieldName();
      if (rfn != null) {
        set.add(rfn);
      }
      projection.entrySet().stream()
          .filter(e -> ConversionUtils.toBoolean(e.getValue()))
          .map(Map.Entry::getKey)
          .forEach(set::add);
      return set;
    } else {
      projection.entrySet().stream()
          .filter(e -> !ConversionUtils.toBoolean(e.getValue()))
          .filter(e -> !mapping.getIdFieldName().equals(e.getKey()))
          .filter(e -> mapping.getRevisionFieldName() != null && !mapping.getRevisionFieldName().equals(e.getKey()))
          .map(Map.Entry::getKey)
          .forEach(set::remove);
      return set;
    }
  }

  private String extractSort(Query query) {
    if (query.getSort() == null) {
      return "";
    }
    List<String> list = new ArrayList<>();
    for (Order order : query.getSort()) {
      String fieldName = order.getProperty();
      String direction = order.isAscending() ? "ASC" : "DESC";
      list.add(String.format("`%s` %s", fieldName, direction));
    }
    return " ORDER BY " + StringUtils.join(list, ",");
  }

  private String extractLimit(Query query) {
    if (query.getLimit() == null && query.getSkip() == null) {
      return "";
    }
    if (query.getLimit() != null && query.getSkip() == null) {
      return String.format(" LIMIT %d", query.getLimit());
    }
    if (query.getLimit() != null && query.getSkip() != null) {
      return String.format(" LIMIT %d,%d", query.getSkip(), query.getLimit());
    }
    throw new UnsupportedOperationException();
  }

}
