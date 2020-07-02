package org.goblinframework.dao.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

abstract public class PersistenceListenerSupport<E, ID> extends PersistenceEntityMappingSupport<E, ID> {

  protected final List<BeforeInsertListener<E>> beforeInsertListeners = new LinkedList<>();
  protected final List<BeforeReplaceListener<E>> beforeReplaceListeners = new LinkedList<>();

  protected PersistenceListenerSupport() {
    registerBeforeInsertListener(e -> {
      long millis = System.currentTimeMillis();
      touchCreateTime(e, millis);
      touchUpdateTime(e, millis);
      initializeRevision(e);
    });
    registerBeforeReplaceListener(e -> {
      long millis = System.currentTimeMillis();
      touchUpdateTime(e, millis);
    });
  }

  protected void registerBeforeInsertListener(@NotNull BeforeInsertListener<E> listener) {
    beforeInsertListeners.add(0, listener);
  }

  protected void registerBeforeReplaceListener(@NotNull BeforeReplaceListener<E> listener) {
    beforeReplaceListeners.add(0, listener);
  }

  protected void beforeInsert(@NotNull E entity) {
    for (BeforeInsertListener<E> listener : beforeInsertListeners) {
      listener.beforeInsert(entity);
    }
  }

  protected void beforeReplace(@NotNull E entity) {
    for (BeforeReplaceListener<E> listener : beforeReplaceListeners) {
      listener.beforeReplace(entity);
    }
  }
}
