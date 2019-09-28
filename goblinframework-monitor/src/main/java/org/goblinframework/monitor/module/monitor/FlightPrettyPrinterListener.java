package org.goblinframework.monitor.module.monitor;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.event.GoblinEventChannel;
import org.goblinframework.api.event.GoblinEventContext;
import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.api.function.Ordered;
import org.goblinframework.api.monitor.Flight;
import org.goblinframework.core.conversion.ConversionUtils;
import org.goblinframework.core.monitor.FlightEvent;
import org.goblinframework.monitor.flight.FlightImpl;
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
    if (ConversionUtils.toBoolean(flight.attribute("flight.silence"))) {
      return;
    }
    if (flight instanceof FlightImpl) {
      String message = FlightRecorderPrinter.generatePrettyLog((FlightImpl) flight);
      logger.info("\n{}", message);
    }
  }
}
