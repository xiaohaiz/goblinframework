package org.goblinframework.remote.core.filter;

import org.jetbrains.annotations.NotNull;

public interface RemoteFilter<E> {

  void filter(@NotNull E context, @NotNull RemoteFilterChain<E> chain);

}
