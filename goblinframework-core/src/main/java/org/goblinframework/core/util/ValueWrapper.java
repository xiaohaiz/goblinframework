package org.goblinframework.core.util;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ValueWrapper<E> {

  @Nullable
  E getValue();

}
