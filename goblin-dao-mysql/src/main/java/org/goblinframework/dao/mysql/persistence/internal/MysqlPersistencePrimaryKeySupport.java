package org.goblinframework.dao.mysql.persistence.internal;

import org.bson.types.ObjectId;
import org.goblinframework.api.dao.Id;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mapping.EntityIdField;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

abstract public class MysqlPersistencePrimaryKeySupport<E, ID> extends MysqlPersistenceTableSupport<E, ID> {

  private static final EnumSet<Id.Generator> supported;

  static {
    supported = EnumSet.of(Id.Generator.NONE, Id.Generator.AUTO_INC, Id.Generator.OBJECT_ID);
  }

  protected final Id.Generator idGenerator;

  protected MysqlPersistencePrimaryKeySupport() {
    EntityIdField idField = entityMapping.idField;
    assert idField != null;
    Id annotation = idField.getAnnotation(Id.class);
    assert annotation != null;

    Id.Generator generator = annotation.value();
    if (!supported.contains(generator)) {
      throw new GoblinMysqlPersistenceException("Id generator not supported: " + generator);
    }
    this.idGenerator = generator;

    registerBeforeInsertListener(e -> {
      generateEntityId(e);
      requireEntityId(e);
    });
  }

  protected void generateEntityId(@NotNull E entity) {
    ID id = getEntityId(entity);
    if (id == null && idGenerator == Id.Generator.OBJECT_ID) {
      Class<?> idClass = entityMapping.idClass;
      if (idClass == ObjectId.class) {
        entityMapping.setId(entity, new ObjectId());
      } else if (idClass == String.class) {
        entityMapping.setId(entity, RandomUtils.nextObjectId());
      }
    }
  }

  protected void requireEntityId(@NotNull E entity) {
    if (idGenerator == Id.Generator.AUTO_INC) {
      return;
    }
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMysqlPersistenceException("Entity id is required");
    }
  }
}
