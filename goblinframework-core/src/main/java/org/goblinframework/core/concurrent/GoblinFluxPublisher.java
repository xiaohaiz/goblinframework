package org.goblinframework.core.concurrent;

import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class GoblinFluxPublisher<E> implements Publisher<E> {

  private final EmitterProcessor<E> emitter;
  private final Flux<E> flux;

  public GoblinFluxPublisher(@Nullable Scheduler scheduler) {
    emitter = EmitterProcessor.create();
    Flux<E> flux = Flux.from(emitter);
    if (scheduler != null) {
      flux = flux.publishOn(scheduler);
    }
    this.flux = flux;
  }

  public void onNext(E value) {
    emitter.onNext(value);
  }

  public void onComplete(@Nullable Throwable cause) {
    if (cause != null) {
      emitter.onError(cause);
    } else {
      emitter.onComplete();
    }
  }

  @Override
  public void subscribe(Subscriber<? super E> s) {
    flux.subscribe(s);
  }
}
