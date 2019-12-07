package org.goblinframework.core.monitor;

import org.goblinframework.api.function.Block0;
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

  private static final ThreadLocal<FlightId> threadLocal = new ThreadLocal<>();
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

  @Nullable
  public static Flight currentThreadFlight() {
    if (flightMonitor == null) {
      return null;
    }
    return flightMonitor.currentFlight();
  }

  public static FlightId currentThreadFlightId() {
    return threadLocal.get();
  }

  public static void dot(@NotNull String name) {
    if (flightMonitor != null) {
      flightMonitor.dot(name);
    }
  }

  public static void executeWithFlight(@Nullable Flight flight, @NotNull Block0 action) {
    if (flight == null) {
      action.apply();
    } else {
      FlightId flightId = flight.flightId();
      threadLocal.set(flightId);
      try {
        action.apply();
      } finally {
        threadLocal.remove();
      }
    }
  }

}
