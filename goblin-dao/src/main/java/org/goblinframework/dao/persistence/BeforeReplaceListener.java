package org.goblinframework.dao.persistence;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BeforeReplaceListener<E> {

  void beforeReplace(@NotNull E entity);

}
