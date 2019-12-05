package org.goblinframework.database.mysql.support;

import org.goblinframework.database.core.DatabaseSystem;
import org.goblinframework.database.core.mapping.EntityMappingBuilder;
import org.goblinframework.database.core.mapping.EntityMappingBuilderManager;
import org.goblinframework.database.core.support.EntityMappingSupport;
import org.jetbrains.annotations.NotNull;

abstract public class MysqlEntityMappingSupport<E, ID> extends EntityMappingSupport<E, ID> {

  @NotNull
  @Override
  protected EntityMappingBuilder getEntityMappingBuilder() {
    EntityMappingBuilder builder = EntityMappingBuilderManager.INSTANCE.getEntityMappingBuilder(DatabaseSystem.MSQ);
    assert builder != null;
    return builder;
  }
}
