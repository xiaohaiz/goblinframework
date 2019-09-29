package org.goblinframework.core.concurrent;

import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class GoblinMonoPublisher<E> implements Publisher<E> {

  private final EmitterProcessor<E> emitter;
  private final Mono<E> mono;

  public GoblinMonoPublisher(@Nullable Scheduler scheduler) {
    emitter = EmitterProcessor.create();
    Mono<E> mono = Mono.from(emitter);
    if (scheduler != null) {
      mono = mono.publishOn(scheduler);
    }
    this.mono = mono;
  }

  public void onComplete(@Nullable E data) {
    onComplete(data, null);
  }

  public void onComplete(@Nullable E data, @Nullable Throwable cause) {
    if (cause != null) {
      emitter.onError(cause);
    } else {
      emitter.onNext(data);
      emitter.onComplete();
    }
  }

  @Override
  public void subscribe(Subscriber<? super E> s) {
    mono.subscribe(s);
  }
}
