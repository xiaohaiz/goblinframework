package org.goblinframework.dao.mongo.persistence.internal;

import org.bson.types.ObjectId;
import org.goblinframework.api.dao.Id;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mapping.EntityIdField;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

abstract public class MongoPersistencePrimaryKeySupport<E, ID> extends MongoPersistenceNamespaceSupport<E, ID> {

  private static final EnumSet<Id.Generator> supportedIdGenerators;

  static {
    supportedIdGenerators = EnumSet.of(Id.Generator.NONE, Id.Generator.OBJECT_ID);
  }

  protected final Id.Generator idGenerator;

  protected MongoPersistencePrimaryKeySupport() {
    EntityIdField idField = entityMapping.idField;
    Id annotation = idField.getAnnotation(Id.class);
    assert annotation != null;
    this.idGenerator = annotation.value();
    if (!supportedIdGenerators.contains(this.idGenerator)) {
      throw new GoblinMongoPersistenceException("Id.Generator [" + this.idGenerator + "] not allowed");
    }
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
      } else {
        throw new UnsupportedOperationException();
      }
    }
  }

  protected void requireEntityId(@NotNull E entity) {
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMongoPersistenceException("Entity id is required");
    }
  }
}
