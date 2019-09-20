package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.core.monitor.Flight;
import org.goblinframework.core.monitor.FlightLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
final public class FlightMonitor implements org.goblinframework.core.monitor.FlightMonitor, Ordered {

  public static final FlightMonitor INSTANCE = new FlightMonitor();

  private FlightMonitor() {
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  @Override
  public void createFlight(@NotNull String flightId, @NotNull FlightLocation location) {
  }

  @Nullable
  @Override
  public Flight terminateFlight() {
    return null;
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
