package org.goblinframework.remote.client.invocation.java;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.api.concurrent.GoblinFutureListener;
import org.goblinframework.core.reactor.CoreScheduler;
import org.goblinframework.core.reactor.SingleResultPublisher;
import org.jetbrains.annotations.NotNull;

final public class RemoteJavaClientInvokerPublisher extends SingleResultPublisher<Object> {

  RemoteJavaClientInvokerPublisher(@NotNull RemoteJavaClientInvokerFuture future) {
    super(CoreScheduler.INSTANCE.get());
    future.addListener(new GoblinFutureListener<Object>() {
      @Override
      public void futureCompleted(@NotNull GoblinFuture<Object> future) {
        Object result = null;
        Throwable cause = null;
        try {
          result = future.get();
        } catch (Throwable ex) {
          cause = ex;
        }
        complete(result, cause);
      }
    });
  }
}
