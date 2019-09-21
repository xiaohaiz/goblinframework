package org.goblinframework.core.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Goblin internal SPI interface. For more information of
 * its implementation, please refer to monitor module.
 */
public interface FlightMonitor {

  @NotNull
  FlightId createFlight(@NotNull FlightLocation location);

  @Nullable
  Flight terminateFlight();

}
