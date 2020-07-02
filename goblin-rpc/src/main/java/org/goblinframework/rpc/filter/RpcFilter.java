package org.goblinframework.rpc.filter;

import org.jetbrains.annotations.NotNull;

public interface RpcFilter<E> {

  void filter(@NotNull E context, @NotNull RpcFilterChain<E> chain);

}
