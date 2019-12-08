package org.goblinframework.database.mysql.eql;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mapping.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCreatorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

final public class MysqlInsertOperation {

  private final MutableBoolean includeId = new MutableBoolean();
  private final LinkedHashMap<String, Object> insertion = new LinkedHashMap<>();
  private final String tableName;

  public MysqlInsertOperation(@NotNull EntityMapping mapping,
                              @NotNull Object entity,
                              @NotNull String tableName) {
    this.tableName = tableName;

    Object id = mapping.idField.getValue(entity);
    if (id != null) {
      includeId.setTrue();
      insertion.put(mapping.getIdFieldName(), EntityFieldValueTranslator.translate(id));
    }
    for (EntityCreateTimeField field : mapping.createTimeFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        insertion.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    for (EntityUpdateTimeField field : mapping.updateTimeFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        insertion.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    for (EntityNormalField field : mapping.normalFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        insertion.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    for (EntityEmbedField field : mapping.embedFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        insertion.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    if (mapping.revisionField != null) {
      Object value = mapping.revisionField.getValue(entity);
      if (value != null) {
        insertion.put(mapping.revisionField.getName(), EntityFieldValueTranslator.translate(value));
      } else {
        ConversionService conversionService = ConversionService.INSTANCE;
        value = conversionService.convert("1", mapping.idField.getField().getFieldType());
        insertion.put(mapping.revisionField.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
  }

  public String generateSQL() {
    String sql;
    if (!hasContent()) {
      sql = String.format("INSERT INTO `%s` VALUES ()", tableName);
    } else {
      sql = String.format("INSERT INTO `%s` (%s) VALUES (%s)", tableName, toFields(), toQuestions());
    }
    return sql;
  }

  public boolean isIncludeId() {
    return includeId.booleanValue();
  }

  public boolean hasContent() {
    return !insertion.isEmpty();
  }

  private String toFields() {
    if (!hasContent()) {
      return "";
    }
    List<String> fields = insertion.keySet().stream()
        .map(e -> "`" + e + "`")
        .collect(Collectors.toList());
    return StringUtils.join(fields, ",");
  }

  private String toQuestions() {
    if (!hasContent()) {
      return "";
    }
    String questions = StringUtils.repeat("?,", insertion.size());
    return questions.substring(0, questions.length() - 1);
  }

  public Object[] toParams() {
    return insertion.values().toArray(new Object[0]);
  }

  public PreparedStatementCreatorFactory newPreparedStatementCreatorFactory(String sql) {
    PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(sql);
    for (Object value : insertion.values()) {
      int parameterType = StatementCreatorUtils.javaTypeToSqlParameterType(value.getClass());
      factory.addParameter(new SqlParameter(parameterType));
    }
    return factory;
  }
}
