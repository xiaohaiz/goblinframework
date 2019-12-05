package org.goblinframework.database.mysql.support;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

abstract public class MysqlListenerSupport<E, ID> extends MysqlPrimaryKeySupport<E, ID> {

  protected final List<BeforeInsertListener<E>> beforeInsertListeners = new LinkedList<>();

  public void registerBeforeInsertListener(@NotNull BeforeInsertListener<E> listener) {
    beforeInsertListeners.add(listener);
  }
}
