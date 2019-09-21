package org.goblinframework.monitor.module.monitor.event;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.goblinframework.core.monitor.FlightEvent;
import org.jetbrains.annotations.NotNull;

@Install
final public class FlightPrettyPrinterListener implements GoblinEventListener, Ordered {

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
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
