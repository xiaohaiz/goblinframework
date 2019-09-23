package org.goblinframework.monitor.module.monitor;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.api.monitor.Flight;
import org.goblinframework.core.event.GoblinEventChannel;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.goblinframework.core.monitor.FlightEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Install
@GoblinEventChannel("/goblin/monitor")
final public class FlightPrettyPrinterListener implements GoblinEventListener, Ordered {
  private static final Logger logger = LoggerFactory.getLogger(FlightPrettyPrinterListener.class);

  public static final FlightPrettyPrinterListener INSTANCE = new FlightPrettyPrinterListener();

  private FlightPrettyPrinterListener() {
  }

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
    Flight flight = event.getFlight();
    if (flight instanceof org.goblinframework.monitor.flight.Flight) {
      String message = FlightRecorderPrinter.generatePrettyLog((org.goblinframework.monitor.flight.Flight) flight);
      logger.info("\n{}", message);
    }
  }
}
