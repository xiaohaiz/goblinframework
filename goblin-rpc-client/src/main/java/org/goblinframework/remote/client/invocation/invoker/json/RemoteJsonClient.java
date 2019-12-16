package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.remote.client.module.exception.ClientInvocationException;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

final public class RemoteJsonClient {

  @NotNull RemoteServiceId serviceId;

  RemoteJsonClient(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
  }

  @NotNull
  RemoteJsonClientInvokerFuture invoke(@NotNull RemoteJsonClientInvocation invocation) {
    RemoteJsonClientInvoker invoker = new RemoteJsonClientInvoker(serviceId);
    try {
      RemoteJsonClientInvokerFuture future = (RemoteJsonClientInvokerFuture) invoker.invoke(invocation);
      assert future != null;
      return future;
    } catch (Throwable ex) {
      throw new ClientInvocationException(ex);
    }
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder builder() {
    return new RemoteJsonClientInvocationBuilder(this);
  }
}
