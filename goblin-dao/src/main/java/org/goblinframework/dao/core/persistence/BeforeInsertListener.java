package org.goblinframework.dao.core.persistence;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BeforeInsertListener<E> {

  void beforeInsert(@NotNull E entity);

}
