package org.goblinframework.dao.mongo.persistence;

import org.goblinframework.dao.mongo.persistence.internal.MongoCachedPersistenceSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class GoblinCachedStaticDao<E, ID> extends MongoCachedPersistenceSupport<E, ID> {

  protected GoblinCachedStaticDao() {
    if (isDynamicDatabase() || isDynamicCollection()) {
      throw new IllegalArgumentException("Dynamic database/collection not allowed");
    }
  }

  @Nullable
  @Override
  public String calculateDatabaseName(@NotNull String template, E entity) {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public String calculateCollectionName(@NotNull String template, E entity) {
    throw new UnsupportedOperationException();
  }
}
