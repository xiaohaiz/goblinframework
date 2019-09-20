package org.goblinframework.dao.mysql.support;

import org.jetbrains.annotations.NotNull;

public interface BeforeInsertListener<E> {

  void beforeInsert(@NotNull E entity);

}
