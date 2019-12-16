package org.goblinframework.remote.client.invocation;

import org.goblinframework.remote.client.module.config.RemoteClientConfig;
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.remote.client.service.RemoteServiceClient;
import org.goblinframework.remote.client.service.RemoteServiceClientManager;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class RemoteClientInvoker<E extends RemoteClientInvocation> {

  @NotNull protected final RemoteServiceId serviceId;
  @NotNull protected final RemoteServiceClient client;
  @NotNull protected final RemoteClientConfig clientConfig;

  protected RemoteClientInvoker(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
    this.client = RemoteServiceClientManager.INSTANCE.getRemoteService(this.serviceId);
    this.client.future().awaitUninterruptibly();
    this.clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig();
  }

  @Nullable
  final public Object invoke(@NotNull E invocation) throws Throwable {
    invocation.maxTimeout = clientConfig.getMaxTimeout();
    invocation.client = client;
    invocation.instruction = new RIC();
    invocation.initializeFuture();
    return doInvoke(invocation);
  }

  @Nullable
  abstract protected Object doInvoke(@NotNull E invocation) throws Throwable;
}
