package org.goblinframework.dao.mysql.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.dao.mysql.annotation.MysqlPersistenceTable;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;
import java.util.Objects;

abstract public class MysqlPersistenceTableSupport<E, ID> extends MysqlPersistenceConnectionSupport<E, ID> {

  private final String table;
  private final boolean dynamic;

  protected MysqlPersistenceTableSupport() {
    MysqlPersistenceTable annotation = AnnotationUtils.getAnnotation(getClass(), MysqlPersistenceTable.class);
    if (annotation == null) {
      throw new GoblinMysqlPersistenceException("No @GoblinTable presented");
    }
    this.table = annotation.table();
    this.dynamic = annotation.dynamic();
  }

  protected boolean isDynamicTable() {
    return dynamic;
  }

  protected LinkedMultiValueMap<String, E> groupEntities(Collection<E> entities) {
    LinkedMultiValueMap<String, E> map = new LinkedMultiValueMap<>();
    if (entities == null || entities.isEmpty()) {
      return map;
    }
    for (E entity : entities) {
      String tableName = getEntityTableName(entity);
      map.add(tableName, entity);
    }
    return map;
  }

  protected LinkedMultiValueMap<String, ID> groupIds(Collection<ID> ids) {
    LinkedMultiValueMap<String, ID> map = new LinkedMultiValueMap<>();
    if (ids == null || ids.isEmpty()) {
      return map;
    }
    for (ID id : ids) {
      String tableName = getIdTableName(id);
      map.add(tableName, id);
    }
    return map;
  }

  @NotNull
  protected String getIdTableName(@Nullable ID id) {
    if (!dynamic) {
      return table;
    } else {
      // 必须根据主键计算分表的才能调用这里
      Objects.requireNonNull(id);
      E entity = newEntityInstance();
      setEntityId(entity, id);
      return calculateTableName(table, entity);
    }
  }

  @NotNull
  protected String getEntityTableName(@Nullable E entity) {
    if (!dynamic) {
      return table;
    } else {
      Objects.requireNonNull(entity);
      return calculateTableName(table, entity);
    }
  }

  @NotNull
  abstract protected String calculateTableName(@NotNull String template, @NotNull E document);
}
