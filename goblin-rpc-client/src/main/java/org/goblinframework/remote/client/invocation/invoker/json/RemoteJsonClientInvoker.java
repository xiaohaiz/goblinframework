package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.remote.client.dispatcher.request.RemoteClientRequestDispatcher;
import org.goblinframework.remote.client.invocation.RemoteClientInvoker;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class RemoteJsonClientInvoker extends RemoteClientInvoker<RemoteJsonClientInvocation> {

  RemoteJsonClientInvoker(@NotNull RemoteServiceId serviceId) {
    super(serviceId);
  }

  @Nullable
  @Override
  protected Object doInvoke(@NotNull RemoteJsonClientInvocation invocation) {
    RemoteClientRequestDispatcher.INSTANCE.onRequest(invocation);
    return invocation.future;
  }
}
