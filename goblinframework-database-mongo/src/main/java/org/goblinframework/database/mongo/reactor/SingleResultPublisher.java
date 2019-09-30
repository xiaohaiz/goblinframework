package org.goblinframework.database.mongo.reactor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.atomic.AtomicBoolean;

final public class SingleResultPublisher<T> implements Publisher<T> {

  private final TopicProcessor<T> topic;
  private final Flux<T> flux;
  private final AtomicBoolean completed = new AtomicBoolean();

  public SingleResultPublisher() {
    this.topic = TopicProcessor.create();
    this.flux = Flux.from(topic).publishOn(MongoSchedulerManager.INSTANCE.getScheduler());
  }

  public SingleResultPublisher<T> complete(@NotNull T value) {
    return complete(value, null);
  }

  public SingleResultPublisher<T> complete(@Nullable T value, @Nullable Throwable cause) {
    if (cause != null) {
      if (!completed.compareAndSet(false, true)) {
        throw new IllegalStateException();
      }
      topic.onError(cause);
    } else if (value != null) {
      if (!completed.compareAndSet(false, true)) {
        throw new IllegalStateException();
      }
      topic.onNext(value);
      topic.onComplete();
    }
    return this;
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    Flux.from(flux).subscribe(s);
  }
}
