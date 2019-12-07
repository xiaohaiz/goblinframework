package org.goblinframework.database.mysql.support;

import org.goblinframework.dao.mysql.mapping.MysqlEntityMappingBuilder;
import org.goblinframework.database.core.mapping.EntityMappingBuilder;
import org.goblinframework.database.core.support.EntityMappingSupport;
import org.jetbrains.annotations.NotNull;

abstract public class MysqlEntityMappingSupport<E, ID> extends EntityMappingSupport<E, ID> {

  @NotNull
  @Override
  protected EntityMappingBuilder getEntityMappingBuilder() {
    return MysqlEntityMappingBuilder.INSTANCE;
  }
}
