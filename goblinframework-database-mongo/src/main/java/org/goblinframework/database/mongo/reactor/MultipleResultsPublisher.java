package org.goblinframework.database.mongo.reactor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.atomic.AtomicReference;

public class MultipleResultsPublisher<T> implements Publisher<T> {

  private final AtomicReference<TopicProcessor<T>> topic = new AtomicReference<>();
  private final Flux<T> flux;

  public MultipleResultsPublisher() {
    TopicProcessor<T> processor = TopicProcessor.create();
    this.topic.set(processor);
    this.flux = Flux.from(processor).publishOn(MongoSchedulerManager.INSTANCE.getScheduler());
  }

  public void onNext(@NotNull T value) {
    TopicProcessor<T> processor = topic.get();
    if (processor != null) {
      processor.onNext(value);
    }
  }

  public void complete(@Nullable Throwable cause) {
    TopicProcessor<T> processor = topic.getAndSet(null);
    if (processor != null) {
      if (cause != null) {
        processor.onError(cause);
      } else {
        processor.onComplete();
      }
    }
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    Flux.from(flux).subscribe(s);
  }
}
