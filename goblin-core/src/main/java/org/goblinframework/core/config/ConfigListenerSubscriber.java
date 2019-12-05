package org.goblinframework.core.config;

import org.goblinframework.api.function.Disposable;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.Instant;

final class ConfigListenerSubscriber implements Subscriber<Instant>, Disposable {

  private boolean cancelled;
  private Subscription subscription;
  private final ConfigListener listener;

  ConfigListenerSubscriber(@NotNull ConfigListener listener) {
    this.listener = listener;
  }

  @Override
  public void onSubscribe(Subscription s) {
    this.subscription = s;
    if (!cancelled) {
      s.request(Long.MAX_VALUE);
    }
  }

  @Override
  public void onNext(Instant instant) {
    listener.onConfigChanged();
  }

  @Override
  public void onError(Throwable t) {
  }

  @Override
  public void onComplete() {
  }

  @Override
  public void dispose() {
    cancelled = true;
    Subscription s = subscription;
    if (s != null) {
      subscription = null;
      s.cancel();
    }
  }
}
