package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@GoblinEventChannel("/goblin/timer")
abstract public class SecondTimerEventListener implements GoblinEventListener {

  @Override
  public boolean accept(@NotNull GoblinEventContext context) {
    if (!(context.getEvent() instanceof GoblinTimerEvent)) {
      return false;
    }
    GoblinTimerEvent event = (GoblinTimerEvent) context.getEvent();
    if (event.getUnit() != TimeUnit.SECONDS) {
      return false;
    }
    long period = periodSeconds();
    if (period <= 1) {
      return true;
    }
    return (event.getSequence() % period) == 0;
  }

  protected long periodSeconds() {
    return 0;
  }
}
