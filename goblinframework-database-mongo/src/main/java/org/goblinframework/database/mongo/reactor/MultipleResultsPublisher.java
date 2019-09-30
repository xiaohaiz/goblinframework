package org.goblinframework.database.mongo.reactor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.atomic.AtomicBoolean;

public class MultipleResultsPublisher<T> implements Publisher<T> {

  private final TopicProcessor<T> topic;
  private final Flux<T> flux;
  private final AtomicBoolean completed = new AtomicBoolean();

  public MultipleResultsPublisher() {
    this.topic = TopicProcessor.create();
    this.flux = Flux.from(topic).publishOn(MongoSchedulerManager.INSTANCE.getScheduler());
  }

  public void onNext(@NotNull T value) {
    topic.onNext(value);
  }

  public void complete(@Nullable Throwable cause) {
    if (!completed.compareAndSet(false, true)) {
      throw new IllegalStateException();
    }
    if (cause != null) {
      topic.onError(cause);
    } else {
      topic.onComplete();
    }
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    Flux.from(flux).subscribe(s);
  }
}
