package org.goblinframework.remote.core.filter;

import org.jetbrains.annotations.NotNull;

public interface RemoteFilterChain<E> {

  void filter(@NotNull E context);

}
