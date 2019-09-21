package org.goblinframework.core.monitor;

import org.goblinframework.core.util.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade utilities of monitor mechanism.
 */
abstract public class FlightRecorder {
  private static final Logger logger = LoggerFactory.getLogger(FlightRecorder.class);

  private static final FlightMonitor flightMonitor;

  static {
    flightMonitor = ServiceInstaller.installedFirst(FlightMonitor.class);
    if (flightMonitor == null) {
      logger.debug("No FlightMonitor installed, ignore monitor");
    }
  }

  @Nullable
  static FlightMonitor getFlightMonitor() {
    return flightMonitor;
  }

  @Nullable
  public static Flight terminateFlight() {
    if (flightMonitor != null) {
      return flightMonitor.terminateFlight();
    } else {
      return null;
    }
  }

  public static void attachFlight(@NotNull Instruction instruction) {
    if (flightMonitor != null) {
      flightMonitor.attachFlight(instruction);
    }
  }

  public static void attachFlight(@Nullable FlightId flightId, @NotNull Instruction instruction) {
    if (flightMonitor != null) {
      flightMonitor.attachFlight(flightId, instruction);
    }
  }

  public static void dot(@NotNull String name) {
    if (flightMonitor != null) {
      flightMonitor.dot(name);
    }
  }

}
