package org.goblinframework.remote.client.invocation.invoker.java;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.api.reactor.GoblinPublisher;
import org.goblinframework.remote.client.dispatcher.request.RemoteClientRequestDispatcher;
import org.goblinframework.remote.client.invocation.RemoteClientInvoker;
import org.goblinframework.remote.client.module.exception.ServerBackPressureException;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class RemoteJavaClientInvoker extends RemoteClientInvoker<RemoteJavaClientInvocation> {

  RemoteJavaClientInvoker(@NotNull RemoteServiceId serviceId) {
    super(serviceId);
  }

  @Nullable
  @Override
  protected Object doInvoke(@NotNull RemoteJavaClientInvocation invocation) {
    if (invocation.retries() == 0) {
      RemoteClientRequestDispatcher.INSTANCE.onRequest(invocation);
      if (invocation.asynchronous()) {
        Class<?> returnType = invocation.method.getReturnType();
        if (returnType == GoblinFuture.class) {
          return invocation.future;
        } else if (returnType == GoblinPublisher.class) {
          return invocation.future.asPublisher();
        } else {
          throw new UnsupportedOperationException();
        }
      } else {
        return invocation.future.get();
      }
    } else {
      int current = 0;
      while (true) {
        RemoteClientRequestDispatcher.INSTANCE.onRequest(invocation);
        try {
          return invocation.future.get();
        } catch (ServerBackPressureException ex) {
          if ((current++) < invocation.retries()) {
            invocation.reset();
            invocation.maxTimeout = clientConfig.getMaxTimeout();
            invocation.client = client;
            invocation.instruction = new RIC();
            invocation.initializeFuture();
            continue;
          }
          throw ex;
        }
      }
    }
  }
}
