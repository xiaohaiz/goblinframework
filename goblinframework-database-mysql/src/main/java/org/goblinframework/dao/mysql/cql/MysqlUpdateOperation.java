package org.goblinframework.dao.mysql.cql;

import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.core.mapping.EntityMapping;
import org.goblinframework.dao.core.mapping.field.EntityEmbedField;
import org.goblinframework.dao.core.mapping.field.EntityNormalField;
import org.goblinframework.dao.core.mapping.field.EntityRevisionField;
import org.goblinframework.dao.core.mapping.field.EntityUpdateTimeField;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MysqlUpdateOperation {

  private final String tableName;
  private final Map<String, Object> updating = new LinkedHashMap<>();
  private final String revisionFieldName;

  public MysqlUpdateOperation(@NotNull EntityMapping mapping,
                              @NotNull Object entity,
                              @NotNull String tableName) {
    this.tableName = tableName;

    for (EntityUpdateTimeField field : mapping.updateTimeFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        updating.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    for (EntityNormalField field : mapping.normalFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        updating.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    for (EntityEmbedField field : mapping.embedFields) {
      Object value = field.getValue(entity);
      if (value != null) {
        updating.put(field.getName(), EntityFieldValueTranslator.translate(value));
      }
    }
    EntityRevisionField revisionField = mapping.revisionField;
    if (revisionField != null) {
      revisionFieldName = revisionField.getName();
    } else {
      revisionFieldName = null;
    }
  }

  public TranslatedCriteria generateSQL() {
    if (!hasContent()) {
      return null;
    }
    ParameterNameGenerator generator = new ParameterNameGenerator("u");
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    List<String> list = new ArrayList<>();
    updating.entrySet().forEach(e -> {
      String f = "`" + e.getKey() + "`";
      String p = generator.next();
      f = f + "=:" + p;
      parameterSource.addValue(p, e.getValue());
      list.add(f);
    });
    if (revisionFieldName != null) {
      String r = "`" + revisionFieldName + "`";
      list.add(r + "=" + r + "+1");
    }

    String sql = "UPDATE `%s` SET %s";
    sql = String.format(sql, tableName, StringUtils.join(list, ","));
    return new TranslatedCriteria(sql, parameterSource);
  }

  public boolean hasContent() {
    return !updating.isEmpty() || revisionFieldName != null;
  }

  public Object[] toParams() {
    return updating.values().toArray(new Object[updating.size()]);
  }

  public String toFields() {
    List<String> list = new ArrayList<>();
    updating.keySet().stream()
        .map(e -> "`" + e + "`")
        .map(e -> e + "=?")
        .forEach(list::add);
    if (revisionFieldName != null) {
      String r = "`" + revisionFieldName + "`";
      list.add(r + "=" + r + "+1");
    }
    if (list.isEmpty()) {
      return "";
    }
    return StringUtils.join(list, ",");
  }
}
