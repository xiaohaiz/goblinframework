package org.goblinframework.api.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExtModuleContext {

  <E> void setExtension(@NotNull String name, @NotNull E value);

  @Nullable
  <E> E getExtension(@NotNull String name);

  @Nullable
  <E> E getExtension(@NotNull Class<E> type);

  @Nullable
  <E> E removeExtension(@NotNull String name);

}
