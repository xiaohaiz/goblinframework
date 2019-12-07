package org.goblinframework.dao.core.listener;

import org.jetbrains.annotations.NotNull;

public interface BeforeInsertListener<E> {

  void beforeInsert(@NotNull E entity);

}
