package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

public class GoblinEventPublisher implements Publisher<GoblinEventContext> {

  private final Mono<GoblinEventContext> mono;

  GoblinEventPublisher(@NotNull GoblinEventFuture future) {
    this.mono = Mono.create(sink -> {
      try {
        GoblinEventContext context = future.getUninterruptibly();
        sink.success(context);
      } catch (Throwable ex) {
        sink.error(ex);
      }
    });
  }

  @Override
  public void subscribe(Subscriber<? super GoblinEventContext> s) {
    mono.subscribe(s);
  }
}
