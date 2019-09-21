package org.goblinframework.monitor.module.test;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.goblinframework.monitor.flight.FlightEvent;
import org.jetbrains.annotations.NotNull;

@Install
final public class UnitTestFlightListener implements GoblinEventListener, Ordered {

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }

  @Override
  public boolean accept(@NotNull GoblinEventContext context) {
    return context.getEvent() instanceof FlightEvent;
  }

  @Override
  public void onEvent(@NotNull GoblinEventContext context) {
    FlightEvent event = (FlightEvent) context.getEvent();
  }
}
