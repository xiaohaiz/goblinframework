package org.goblinframework.core.event.timer;

import org.goblinframework.core.event.GoblinEventChannel;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@GoblinEventChannel("/goblin/timer")
abstract public class MinuteTimerEventListener implements GoblinEventListener {

  @Override
  public boolean accept(@NotNull GoblinEventContext context) {
    if (!(context.getEvent() instanceof GoblinTimerEvent)) {
      return false;
    }
    GoblinTimerEvent event = (GoblinTimerEvent) context.getEvent();
    if (event.getUnit() != TimeUnit.MINUTES) {
      return false;
    }
    long period = periodMinutes();
    if (period <= 1) {
      return true;
    }
    return (event.getSequence() % period) == 0;
  }

  protected long periodMinutes() {
    return 0;
  }
}
