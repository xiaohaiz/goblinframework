package org.goblinframework.api.monitor;

import org.goblinframework.api.core.Internal;
import org.goblinframework.api.core.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Goblin internal SPI interface. For more information of
 * its implementation, please refer to monitor module.
 */
@Internal
public interface IFlightMonitor {

  @NotNull
  FlightId createFlight(@NotNull FlightLocation location);

  @Nullable
  Flight terminateFlight();

  void attachFlight(@NotNull Instruction instruction);

  void attachFlight(@Nullable FlightId flightId, @NotNull Instruction instruction);

  void dot(@Nullable String name);

  @Nullable
  static IFlightMonitor instance() {
    return ServiceInstaller.firstOrNull(IFlightMonitor.class);
  }
}
