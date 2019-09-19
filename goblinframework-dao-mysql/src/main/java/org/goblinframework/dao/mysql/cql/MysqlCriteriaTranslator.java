package org.goblinframework.dao.mysql.cql;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.conversion.ConversionUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.CriteriaTranslator;
import org.goblinframework.dao.core.cql.Operator;
import org.goblinframework.dao.core.cql.OperatorValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
final public class MysqlCriteriaTranslator implements CriteriaTranslator<TranslatedCriteria> {

  public static final MysqlCriteriaTranslator INSTANCE = new MysqlCriteriaTranslator();

  private MysqlCriteriaTranslator() {
  }

  @Override
  public TranslatedCriteria translate(Criteria criteria) {
    TranslatedCriteria tc = doTranslate(criteria);
    if (StringUtils.isNotEmpty(tc.sql)) {
      tc.sql = " WHERE " + tc.sql;
    }
    return tc;
  }

  private TranslatedCriteria doTranslate(Criteria criteria) {
    Objects.requireNonNull(criteria);
    ParameterNameGenerator parameterNameGenerator = new ParameterNameGenerator("c");
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    if (criteria.getJoiner() == null) {
      SQL sql = translateSingleCriteria(parameterNameGenerator, parameterSource, criteria);
      return new TranslatedCriteria(sql.sql, parameterSource);
    } else {
      SQL sql = translateMultipleCriteria(parameterNameGenerator, parameterSource, criteria);
      return new TranslatedCriteria(sql.sql, parameterSource);
    }
  }

  private SQL translateMultipleCriteria(ParameterNameGenerator parameterNameGenerator,
                                        MapSqlParameterSource parameterSource,
                                        Criteria criteria) {
    List<SQL> list = criteria.getCriteriaChain()
        .stream()
        .map(c -> {
          if (c.getJoiner() == null) {
            return translateSingleCriteria(parameterNameGenerator, parameterSource, c);
          } else {
            return translateMultipleCriteria(parameterNameGenerator, parameterSource, c);
          }
        })
        .collect(Collectors.toList());
    if (list.size() == 1) {
      return list.iterator().next();
    } else {
      List<String> s = list.stream()
          .map(e -> {
            if (e.size == 1) {
              return e.sql;
            } else {
              return String.format("(%s)", e.sql);
            }
          })
          .collect(Collectors.toList());
      String sql;
      switch (criteria.getJoiner()) {
        case $and: {
          sql = StringUtils.join(s, " AND ");
          return new SQL(s.size(), sql);
        }
        case $or: {
          sql = StringUtils.join(s, " OR ");
          return new SQL(s.size(), sql);
        }
        default: {
          throw new UnsupportedOperationException();
        }
      }
    }
  }

  private SQL translateSingleCriteria(ParameterNameGenerator parameterNameGenerator,
                                      MapSqlParameterSource parameterSource,
                                      Criteria criteria) {
    Map<String, OperatorValue> chain = criteria.export();
    List<SQL> list = chain.entrySet().stream()
        .map(entry -> translate(parameterNameGenerator, parameterSource, entry))
        .collect(Collectors.toList());
    if (list.size() == 1) {
      return list.iterator().next();
    } else {
      int size = list.stream()
          .mapToInt(e -> e.size)
          .sum();
      List<String> s = list.stream()
          .map(e -> e.sql)
          .collect(Collectors.toList());
      return new SQL(size, StringUtils.join(s, " AND "));
    }
  }

  private SQL translate(ParameterNameGenerator parameterNameGenerator,
                        MapSqlParameterSource parameterSource,
                        Map.Entry<String, OperatorValue> entry) {
    String field = entry.getKey();
    String modifier = "";
    if (StringUtils.contains(field, " ")) {
      modifier = StringUtils.substringBeforeLast(field, " ");
      if (!"".equals(modifier)) {
        modifier = modifier + " ";
      }
      field = StringUtils.substringAfterLast(field, " ");
    }
    List<String> list = new ArrayList<>();
    OperatorValue operatorValue = entry.getValue();
    if (!operatorValue.hasContent()) {
      String message = String.format("No operator found of field '%s'", field);
      throw new IllegalStateException(message);
    }
    for (Map.Entry<Operator, Object> e : operatorValue.entrySet()) {
      Operator operator = e.getKey();
      Object value = e.getValue();
      switch (operator) {
        case $eq: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s`=:%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $gt: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("`%s`>:%s", field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $gte: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s`>=:%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $lt: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s`<:%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $lte: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s`<=:%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $ne: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s`<>:%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $in: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s` IN (:%s)", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $nin: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s` NOT IN (:%s)", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        case $exists: {
          if (ConversionUtils.toBoolean(value)) {
            list.add(String.format("%s`%s` IS NOT NULL", modifier, field));
          } else {
            list.add(String.format("ISNULL(%s`%s`)", modifier, field));
          }
          break;
        }
        case $like: {
          String parameterName = parameterNameGenerator.next();
          list.add(String.format("%s`%s` LIKE :%s", modifier, field, parameterName));
          parameterSource.addValue(parameterName, EntityFieldValueTranslator.translate(value));
          break;
        }
        default: {
          throw new UnsupportedOperationException();
        }
      }
    }
    return new SQL(list.size(), StringUtils.join(list, " AND "));
  }

  private static class SQL {
    public int size;
    public String sql;

    public SQL() {
    }

    public SQL(int size, String sql) {
      this.size = size;
      this.sql = sql;
    }
  }
}
