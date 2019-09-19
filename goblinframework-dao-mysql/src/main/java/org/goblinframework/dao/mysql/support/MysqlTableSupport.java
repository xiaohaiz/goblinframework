package org.goblinframework.dao.mysql.support;

import org.goblinframework.api.annotation.Table;
import org.goblinframework.dao.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  /**
   * 必须根据主键计算分表的才能调用此方法
   */
  @NotNull
  protected String getIdTableName(@Nullable ID id) {
    if (!dynamic) {
      return table;
    } else {
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
