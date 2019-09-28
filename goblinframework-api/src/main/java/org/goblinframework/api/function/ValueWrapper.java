package org.goblinframework.api.function;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ValueWrapper<E> {

  @Nullable
  E getValue();

}
