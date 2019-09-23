package org.goblinframework.api.event;

import org.jetbrains.annotations.NotNull;

final public class EventBus {

  public static void subscribe(@NotNull GoblinEventListener listener) {
    IEventBusManager.instance().subscribe(listener);
  }

  public static void subscribe(@NotNull String channel, @NotNull GoblinEventListener listener) {
    IEventBusManager.instance().subscribe(channel, listener);
  }

  public static void unsubscribe(@NotNull GoblinEventListener listener) {
    IEventBusManager.instance().unsubscribe(listener);
  }

  public static void unsubscribe(@NotNull String channel, @NotNull GoblinEventListener listener) {
    IEventBusManager.instance().unsubscribe(channel, listener);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull GoblinEvent event) {
    return IEventBusManager.instance().publish(event);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull String channel, @NotNull GoblinEvent event) {
    return IEventBusManager.instance().publish(channel, event);
  }

  @NotNull
  public static <E> GoblinCallbackFuture<E> execute(@NotNull GoblinCallback<E> callback) {
    return IEventBusManager.instance().execute(callback);
  }
}
