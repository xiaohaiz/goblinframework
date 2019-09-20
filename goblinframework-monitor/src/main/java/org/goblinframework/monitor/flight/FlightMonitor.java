package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.core.monitor.FlightLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
final public class FlightMonitor implements org.goblinframework.core.monitor.FlightMonitor, Ordered {

  public static final FlightMonitor INSTANCE = new FlightMonitor();

  private final ThreadLocal<Flight> threadLocal = new ThreadLocal<>();

  private FlightMonitor() {
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  @Override
  public void createFlight(@NotNull String flightId, @NotNull FlightLocation location) {
    Flight flight = threadLocal.get();
    if (flight != null) {
      flight.retain();
      return;
    }
    flight = new Flight(flightId, location);
    flight.retain();
    threadLocal.set(flight);
  }

  @Nullable
  @Override
  public Flight terminateFlight() {
    Flight flight = threadLocal.get();
    if (flight == null) {
      return null;
    }
    if (!flight.release()) {
      return null;
    }
    threadLocal.remove();
    // Now, terminate the current flight

    return flight;
  }

  @Install
  final public static class Installer implements org.goblinframework.core.monitor.FlightMonitor {

    @Override
    public void createFlight(@NotNull String flightId, @NotNull FlightLocation location) {
      INSTANCE.createFlight(flightId, location);
    }

    @Nullable
    @Override
    public Flight terminateFlight() {
      return INSTANCE.terminateFlight();
    }
  }
}
