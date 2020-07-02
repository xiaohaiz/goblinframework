package org.goblinframework.core.monitor;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.core.service.ServiceInstaller;
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

  /**
   * Try to lookup Flight of current thread, this operation takes no
   * effects to Flight's reference count.
   *
   * @return Flight of current thread, return null if not Flight found
   */
  @Nullable
  Flight currentFlight();

  void attachFlight(@Nullable FlightId flightId, @NotNull Instruction instruction);

  void dot(@Nullable String name);

  @Nullable
  static IFlightMonitor instance() {
    return ServiceInstaller.firstOrNull(IFlightMonitor.class);
  }
}
