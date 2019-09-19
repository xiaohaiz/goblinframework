package org.goblinframework.dao.mysql.cql;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.goblinframework.dao.core.mapping.EntityMapping;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

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
    }
  }
}
