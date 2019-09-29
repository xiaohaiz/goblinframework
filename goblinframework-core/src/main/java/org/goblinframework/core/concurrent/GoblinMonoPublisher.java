package org.goblinframework.core.concurrent;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class GoblinMonoPublisher<E> implements Publisher<E> {

  private final Mono<E> mono;

  public GoblinMonoPublisher(@NotNull GoblinFuture<E> future, @Nullable Scheduler scheduler) {
    Mono<E> mono = Mono.<E>create(sink -> {
      future.addListener(it -> {
        try {
          E value = it.getUninterruptibly();
          sink.success(value);
        } catch (Throwable ex) {
          sink.error(ex);
        }
      });
    });
    if (scheduler != null) {
      mono = mono.publishOn(scheduler);
    }
    this.mono = mono;
  }

  @Override
  public void subscribe(Subscriber<? super E> s) {
    mono.subscribe(s);
  }
}
