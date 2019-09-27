package org.goblinframework.database.mysql.support;

import org.goblinframework.api.dao.Table;
import org.goblinframework.database.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;
import java.util.Objects;

abstract public class MysqlTableSupport<E, ID> extends MysqlClientSupport<E, ID> {

  private final String table;
  private final boolean dynamic;

  protected MysqlTableSupport() {
    Class<?> entityClass = entityMapping.entityClass;
    Table annotation = entityClass.getAnnotation(Table.class);
    if (annotation == null) {
      throw new GoblinPersistenceException("No @Table presented on entity: " + entityClass.getName());
    }
    this.table = annotation.table();
    this.dynamic = annotation.dynamic();
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
