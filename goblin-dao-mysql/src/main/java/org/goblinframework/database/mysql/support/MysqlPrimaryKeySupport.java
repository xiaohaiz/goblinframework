package org.goblinframework.database.mysql.support;

import org.bson.types.ObjectId;
import org.goblinframework.api.dao.Id;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mysql.persistence.internal.MysqlPersistenceTableSupport;
import org.goblinframework.database.core.mapping.EntityIdField;
import org.goblinframework.database.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

abstract public class MysqlPrimaryKeySupport<E, ID> extends MysqlPersistenceTableSupport<E, ID> {

  private static final EnumSet<Id.Generator> supported;

  static {
    supported = EnumSet.of(Id.Generator.NONE, Id.Generator.AUTO_INC, Id.Generator.OBJECT_ID);
  }

  protected final Id.Generator generator;

  protected MysqlPrimaryKeySupport() {
    EntityIdField idField = entityMapping.idField;
    assert idField != null;
    Id annotation = idField.getAnnotation(Id.class);
    assert annotation != null;

    Id.Generator generator = annotation.value();
    if (!supported.contains(generator)) {
      throw new GoblinPersistenceException("Id generator not supported: " + generator);
    }
    this.generator = generator;
  }

  protected void generateEntityId(@NotNull E entity) {
    ID id = getEntityId(entity);
    if (id == null && generator == Id.Generator.OBJECT_ID) {
      Class<?> idClass = entityMapping.idClass;
      if (idClass == ObjectId.class) {
        entityMapping.setId(entity, new ObjectId());
      } else if (idClass == String.class) {
        entityMapping.setId(entity, RandomUtils.nextObjectId());
      }
    }
  }

  protected void requireEntityId(@NotNull E entity) {
    if (generator == Id.Generator.AUTO_INC) {
      return;
    }
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinPersistenceException("Entity id is required");
    }
  }
}
