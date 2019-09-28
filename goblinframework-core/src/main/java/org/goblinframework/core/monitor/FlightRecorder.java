package org.goblinframework.core.monitor;

import org.goblinframework.core.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade utilities of monitor mechanism.
 */
abstract public class FlightRecorder {
  private static final Logger logger = LoggerFactory.getLogger(FlightRecorder.class);

  private static final IFlightMonitor flightMonitor;

  static {
    flightMonitor = ServiceInstaller.firstOrNull(IFlightMonitor.class);
    if (flightMonitor == null) {
      logger.debug("No FlightMonitor installed, ignore monitor");
    }
  }

  @Nullable
  public static IFlightMonitor getFlightMonitor() {
    return flightMonitor;
  }

  public static void dot(@NotNull String name) {
    if (flightMonitor != null) {
      flightMonitor.dot(name);
    }
  }

}
