package org.goblinframework.core.reactor;

import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.atomic.AtomicReference;

public class SingleResultPublisher<T> implements Publisher<T> {

  private final AtomicReference<TopicProcessor<T>> topic = new AtomicReference<>();
  private final Flux<T> flux;

  public SingleResultPublisher(@Nullable Scheduler scheduler) {
    TopicProcessor<T> processor = TopicProcessor.create();
    this.topic.set(processor);
    Flux<T> flux = Flux.from(processor);
    if (scheduler != null) {
      flux = flux.publishOn(scheduler);
    }
    this.flux = flux;
  }

  public void complete(@Nullable T value, @Nullable Throwable cause) {
    TopicProcessor<T> processor = this.topic.getAndSet(null);
    if (processor != null) {
      if (cause != null) {
        processor.onError(cause);
        return;
      }
      if (value != null) {
        processor.onNext(value);
      }
      processor.onComplete();
    }
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    Flux.from(flux).subscribe(s);
  }
}
