package org.goblinframework.core.event.timer;

import org.goblinframework.api.event.GoblinEventChannel;
import org.goblinframework.api.event.GoblinEventContext;
import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.core.event.dsl.GoblinTimerEvent;
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
