package org.goblinframework.dao.mongo.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.mongo.persistence.MongoPersistenceDatabase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

abstract public class MongoPersistenceDatabaseSupport<E, ID> extends MongoPersistenceConnectionSupport<E, ID> {

  private final String database;
  private final boolean dynamic;

  protected MongoPersistenceDatabaseSupport() {
    MongoPersistenceDatabase annotation = AnnotationUtils.getAnnotation(getClass(), MongoPersistenceDatabase.class);
    if (annotation == null) {
      throw new GoblinMongoPersistenceException("No @MongoPersistenceDatabase presented");
    }
    this.database = annotation.database();
    this.dynamic = annotation.dynamic();
  }

  final protected boolean isDynamicDatabase() {
    return dynamic;
  }

  @NotNull
  public String getIdDatabaseName(@Nullable ID id) {
    if (!dynamic) {
      return this.database;
    }
    Objects.requireNonNull(id);
    E entity = newEntityInstance();
    setEntityId(entity, id);
    String database = calculateDatabaseName(this.database, entity);
    Objects.requireNonNull(database);
    return database;
  }

  @NotNull
  public String getEntityDatabaseName(@Nullable E entity) {
    if (!dynamic) {
      return this.database;
    }
    Objects.requireNonNull(entity);
    String database = calculateDatabaseName(this.database, entity);
    Objects.requireNonNull(database);
    return database;
  }

  @Nullable
  abstract protected String calculateDatabaseName(@NotNull String template, @NotNull E entity);
}
