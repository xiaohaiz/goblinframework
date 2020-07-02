package org.goblinframework.dao.mysql.mapping;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.dao.mapping.EntityField;
import org.goblinframework.dao.mapping.EntityIdField;
import org.goblinframework.dao.mapping.EntityMapping;
import org.goblinframework.dao.mapping.EntityRevisionField;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MysqlEntityRowMapper<E> implements RowMapper<E> {

  private final ConversionService conversionService = ConversionService.INSTANCE;
  private final EntityMapping mapping;

  public MysqlEntityRowMapper(@NotNull EntityMapping mapping) {
    this.mapping = mapping;
  }

  @SuppressWarnings("unchecked")
  @Override
  public E mapRow(@NotNull ResultSet rs, int rowNum) {
    Object entity = mapping.newInstance();

    List<ImmutablePair<String, EntityField>> candidate = new LinkedList<>();
    EntityIdField idField = mapping.idField;
    candidate.add(new ImmutablePair<>(idField.getName(), idField));
    EntityRevisionField revisionField = mapping.revisionField;
    if (revisionField != null) {
      candidate.add(new ImmutablePair<>(revisionField.getName(), revisionField));
    }
    mapping.createTimeFields.stream()
        .map(f -> new ImmutablePair<String, EntityField>(f.getName(), f))
        .forEach(candidate::add);
    mapping.updateTimeFields.stream()
        .map(f -> new ImmutablePair<String, EntityField>(f.getName(), f))
        .forEach(candidate::add);
    mapping.normalFields.stream()
        .map(f -> new ImmutablePair<String, EntityField>(f.getName(), f))
        .forEach(candidate::add);
    mapping.embedFields.stream()
        .map(f -> new ImmutablePair<String, EntityField>(f.getName(), f))
        .forEach(candidate::add);

    candidate.forEach(p -> {
      Object val = getColumnValue(rs, p.getLeft());
      if (val != null) {
        val = conversionService.convert(val, p.getRight().getField().getFieldType());
        p.getRight().setValue(entity, val);
      }
    });

    return (E) entity;
  }

  private Object getColumnValue(ResultSet rs, String columnLabel) {
    try {
      return rs.getObject(columnLabel);
    } catch (SQLException ex) {
      // maybe the column not exists in result set
      return null;
    }
  }
}
