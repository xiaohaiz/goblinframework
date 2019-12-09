package org.goblinframework.remote.client.invocation.invoker.java;

import org.goblinframework.api.reactor.GoblinPublisher;
import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final public class RemoteJavaClientInvokerPublisher implements GoblinPublisher<Object> {

  @NotNull private final RemoteJavaClientInvokerFuture future;

  RemoteJavaClientInvokerPublisher(@NotNull RemoteJavaClientInvokerFuture future) {
    this.future = future;
  }

  @Override
  public void subscribe(@NotNull Subscriber<? super Object> s) {
    s.onSubscribe(new Subscription() {
      @Override
      public void request(long n) {
        if (n > 0) {
          try {
            Object result = future.get();
            s.onNext(result);
            s.onComplete();
          } catch (Throwable ex) {
            s.onError(ex);
          }
        }
      }

      @Override
      public void cancel() {
      }
    });
  }

  @Nullable
  @Override
  public Object block() {
    BlockingMonoSubscriber<Object> subscriber = new BlockingMonoSubscriber<>();
    subscribe(subscriber);
    try {
      return subscriber.block();
    } finally {
      subscriber.dispose();
    }
  }
}
