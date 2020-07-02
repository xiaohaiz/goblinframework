package org.goblinframework.dao.mysql.ql;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mapping.EntityMapping;
import org.goblinframework.dao.mapping.EntityUpdateTimeField;
import org.goblinframework.dao.ql.FieldValue;
import org.goblinframework.dao.ql.Operator;
import org.goblinframework.dao.ql.Update;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * For translating specified {@link Update} to {@link NamedParameterSQL}.
 * Note: not all operators supported.
 */
@Singleton
final public class MysqlUpdateTranslator {

  public static final MysqlUpdateTranslator INSTANCE = new MysqlUpdateTranslator();

  private MysqlUpdateTranslator() {
  }

  public NamedParameterSQL translate(@NotNull Update update, @Nullable EntityMapping mapping) {
    Objects.requireNonNull(update);

    ParameterNameGenerator generator = new ParameterNameGenerator("u");
    List<String> list = new LinkedList<>();
    MapSqlParameterSource source = new MapSqlParameterSource();

    Map<Operator, FieldValue> map = update.export();
    for (Map.Entry<Operator, FieldValue> entry : map.entrySet()) {
      Operator operator = entry.getKey();
      FieldValue fieldValue = entry.getValue();
      switch (operator) {
        case $inc: {
          for (Map.Entry<String, Object> e : fieldValue.entrySet()) {
            String field = "`" + e.getKey() + "`";
            Object value = e.getValue();
            value = EntityFieldValueTranslator.translate(value);
            String parameterName = generator.next();
            String s = String.format("%s=%s+:%s", field, field, parameterName);
            list.add(s);
            source.addValue(parameterName, value);
          }
          break;
        }
        case $set: {
          for (Map.Entry<String, Object> e : fieldValue.entrySet()) {
            String field = "`" + e.getKey() + "`";
            Object value = e.getValue();
            value = EntityFieldValueTranslator.translate(value);
            String parameterName = generator.next();
            String s = String.format("%s=:%s", field, parameterName);
            list.add(s);
            source.addValue(parameterName, value);
          }
          break;
        }
        case $unset: {
          for (Map.Entry<String, Object> e : fieldValue.entrySet()) {
            String field = "`" + e.getKey() + "`";
            String s = String.format("%s=NULL", field);
            list.add(s);
          }
          break;
        }
        case $currentDate: {
          for (Map.Entry<String, Object> e : fieldValue.entrySet()) {
            String field = "`" + e.getKey() + "`";
            String s = String.format("%s=NOW()", field);
            list.add(s);
          }
          break;
        }
        default: {
          throw new UnsupportedOperationException();
        }
      }
    }
    long millis = System.currentTimeMillis();
    if (mapping != null) {
      ConversionService conversionService = ConversionService.INSTANCE;
      for (EntityUpdateTimeField field : mapping.updateTimeFields) {
        String f = "`" + field.getName() + "`";
        Object value = conversionService.convert(millis, field.getField().getFieldType());
        String parameterName = generator.next();
        String s = String.format("%s=:%s", f, parameterName);
        list.add(s);
        source.addValue(parameterName, value);
      }
      if (mapping.revisionField != null) {
        String f = "`" + mapping.revisionField.getName() + "`";
        String s = String.format("%s=%s+1", f, f);
        list.add(s);
      }
    }
    String sql = StringUtils.join(list, ",");
    return new NamedParameterSQL(sql, source);
  }

}
