package org.goblinframework.core.monitor;

import org.goblinframework.api.function.Block0;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class FlightExecutor {

  @NotNull private final ThreadLocal<FlightId> threadLocal;
  @Nullable private final Flight flight;

  FlightExecutor(@NotNull ThreadLocal<FlightId> threadLocal, @Nullable Flight flight) {
    this.threadLocal = threadLocal;
    this.flight = flight;
  }

  public void execute(@NotNull Block0 action) {
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
