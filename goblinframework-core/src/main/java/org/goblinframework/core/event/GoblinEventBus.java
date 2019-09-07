package org.goblinframework.core.event;

import org.goblinframework.core.event.boss.EventBusBoss;
import org.jetbrains.annotations.NotNull;

final public class GoblinEventBus {

  public static void subscribe(@NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.subscribe(listener);
  }

  public static GoblinEventFuture publish(@NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(event);
  }

  public static GoblinEventFuture publish(@NotNull String channel, @NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(channel, event);
  }
}
