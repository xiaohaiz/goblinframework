package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class GoblinEventPublisher implements Publisher<GoblinEventContext> {

  private final Mono<GoblinEventContext> mono;

  GoblinEventPublisher(@NotNull Scheduler scheduler, @NotNull GoblinEventFuture future) {
    mono = Mono.<GoblinEventContext>create(sink -> {
      try {
        GoblinEventContext context = future.getUninterruptibly();
        sink.success(context);
      } catch (Throwable ex) {
        sink.error(ex);
      }
    }).subscribeOn(scheduler);
  }

  @Override
  public void subscribe(Subscriber<? super GoblinEventContext> s) {
    mono.subscribe(s);
  }
}
