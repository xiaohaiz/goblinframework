package org.goblinframework.dao.mysql.persistence.internal;

import org.goblinframework.dao.mapping.EntityMappingBuilder;
import org.goblinframework.dao.mysql.mapping.MysqlEntityMappingBuilder;
import org.goblinframework.dao.persistence.PersistenceListenerSupport;
import org.jetbrains.annotations.NotNull;

abstract public class MysqlPersistenceEntityMappingSupport<E, ID> extends PersistenceListenerSupport<E, ID> {

  @NotNull
  @Override
  protected EntityMappingBuilder getEntityMappingBuilder() {
    return MysqlEntityMappingBuilder.INSTANCE;
  }
}
