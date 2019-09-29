package org.goblinframework.core.event;

import org.goblinframework.api.function.GoblinCallback;
import org.jetbrains.annotations.NotNull;

final public class EventBus {

  public static void register(@NotNull String channel, int ringBufferSize, int workerHandlers) {
    EventBusBoss.INSTANCE.register(channel, ringBufferSize, workerHandlers);
  }

  public static void unregister(@NotNull String channel) {
    EventBusBoss.INSTANCE.unregister(channel);
  }

  public static void subscribe(@NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.subscribe(listener);
  }

  public static void subscribe(@NotNull String channel, @NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.subscribe(channel, listener);
  }

  public static void unsubscribe(@NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.unsubscribe(listener);
  }

  public static void unsubscribe(@NotNull String channel, @NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.unsubscribe(channel, listener);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(event);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull String channel, @NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(channel, event);
  }

  @NotNull
  public static GoblinEventPublisher publish2(@NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish2(event);
  }

  @NotNull
  public static GoblinEventPublisher publish2(@NotNull String channel, @NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish2(channel, event);
  }

  @NotNull
  public static <E> GoblinCallbackFuture<E> execute(@NotNull GoblinCallback<E> callback) {
    GoblinCallbackFuture<E> future = new GoblinCallbackFuture<>();
    EventBusBoss.INSTANCE.publish(new GoblinCallbackEvent(callback)).addListener(it -> {
      GoblinEventContext context;
      try {
        context = it.getUninterruptibly();
      } catch (Throwable ex) {
        future.complete(null, ex);
        return;
      }
      @SuppressWarnings("unchecked") E result = (E) context.getExtension("GoblinCallback.Result");
      future.complete(result);
    });
    return future;
  }
}
