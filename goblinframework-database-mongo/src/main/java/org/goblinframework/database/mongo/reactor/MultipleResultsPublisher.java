package org.goblinframework.database.mongo.reactor;

import org.goblinframework.api.core.ReferenceCount;
import org.goblinframework.core.util.GoblinReferenceCount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.extra.processor.TopicProcessor;

import java.util.concurrent.atomic.AtomicReference;

public class MultipleResultsPublisher<T> implements Publisher<T>, ReferenceCount {

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

  private final AtomicReference<GoblinReferenceCount> grc = new AtomicReference<>();

  public void initializeCount(int count) {
    grc.set(new GoblinReferenceCount(count));
  }

  @Override
  public int count() {
    GoblinReferenceCount rc = grc.get();
    if (rc == null) throw new IllegalStateException();
    return rc.count();
  }

  @Override
  public void retain() {
    GoblinReferenceCount rc = grc.get();
    if (rc == null) throw new IllegalStateException();
    rc.retain();
  }

  @Override
  public void retain(int increment) {
    GoblinReferenceCount rc = grc.get();
    if (rc == null) throw new IllegalStateException();
    rc.retain(increment);
  }

  @Override
  public boolean release() {
    GoblinReferenceCount rc = grc.get();
    if (rc == null) throw new IllegalStateException();
    boolean ret = rc.release();
    if (ret) complete(null);
    return ret;
  }

  @Override
  public boolean release(int decrement) {
    GoblinReferenceCount rc = grc.get();
    if (rc == null) throw new IllegalStateException();
    boolean ret = rc.release(decrement);
    if (ret) complete(null);
    return ret;
  }
}
