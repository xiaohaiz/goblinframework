package org.goblinframework.dao.core.listener;

import org.goblinframework.dao.core.mapping.EntityMappingSupport;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract public class ListenerSupport<E, ID> extends EntityMappingSupport<E, ID> {

  protected final List<BeforeInsertListener<E>> beforeInsertListeners = new ArrayList<>();

  protected ListenerSupport() {
  }

  public void registerBeforeInsertListener(@NotNull BeforeInsertListener<E> listener) {
    beforeInsertListeners.add(listener);
  }
}
