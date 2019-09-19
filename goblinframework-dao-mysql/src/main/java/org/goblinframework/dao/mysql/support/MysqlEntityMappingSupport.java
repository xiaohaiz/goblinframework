package org.goblinframework.dao.mysql.support;

import org.goblinframework.dao.core.mapping.EntityMappingBuilder;
import org.goblinframework.dao.core.mapping.EntityMappingBuilderManager;
import org.goblinframework.dao.core.module.DatabaseSystem;
import org.goblinframework.dao.core.support.EntityMappingSupport;
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
