package org.goblinframework.rpc.filter;

import org.jetbrains.annotations.NotNull;

public interface RpcFilterChain<E> {

  void filter(@NotNull E context);

}
