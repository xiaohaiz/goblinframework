package org.goblinframework.dao.persistence;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BeforeInsertListener<E> {

  void beforeInsert(@NotNull E entity);

}
