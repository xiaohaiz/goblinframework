package org.goblinframework.remote.client.invocation.java;

import org.goblinframework.api.reactor.GoblinPublisher;
import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.goblinframework.core.reactor.CoreScheduler;
import org.goblinframework.core.reactor.SingleResultPublisher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class RemoteJavaClientInvokerPublisher
    extends SingleResultPublisher<Object> implements GoblinPublisher<Object> {

  RemoteJavaClientInvokerPublisher(@NotNull RemoteJavaClientInvokerFuture future) {
    super(CoreScheduler.INSTANCE.get());
    future.addListener(f -> {
      Object result = null;
      Throwable cause = null;
      try {
        result = f.get();
      } catch (Throwable ex) {
        cause = ex;
      }
      complete(result, cause);
    });
  }

  @Nullable
  @Override
  public Object block() {
    BlockingMonoSubscriber<Object> subscriber = new BlockingMonoSubscriber<>();
    subscribe(subscriber);
    return subscriber.block();
  }
}
