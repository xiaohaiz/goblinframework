package org.goblinframework.dao.mongo.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.dao.mongo.annotation.MongoPersistenceCollection;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

abstract public class MongoPersistenceCollectionSupport<E, ID> extends MongoPersistenceDatabaseSupport<E, ID> {

  private final String collection;
  private final boolean dynamic;

  protected MongoPersistenceCollectionSupport() {
    MongoPersistenceCollection annotation = AnnotationUtils.getAnnotation(getClass(), MongoPersistenceCollection.class);
    if (annotation == null) {
      throw new GoblinMongoPersistenceException("No @MongoPersistenceCollection presented");
    }
    this.collection = annotation.collection();
    this.dynamic = annotation.dynamic();
  }

  public boolean isDynamicCollection() {
    return dynamic;
  }

  @NotNull
  public String getIdCollectionName(@Nullable ID id) {
    if (!dynamic) {
      return this.collection;
    }
    Objects.requireNonNull(id);
    E entity = newEntityInstance();
    setEntityId(entity, id);
    String collection = calculateCollectionName(this.collection, entity);
    Objects.requireNonNull(collection);
    return collection;
  }

  @NotNull
  public String getEntityCollectionName(@Nullable E entity) {
    if (!dynamic) {
      return this.collection;
    }
    Objects.requireNonNull(entity);
    String collection = calculateCollectionName(this.collection, entity);
    Objects.requireNonNull(collection);
    return collection;
  }

  @Nullable
  abstract protected String calculateCollectionName(@NotNull String template, @NotNull E entity);
}
