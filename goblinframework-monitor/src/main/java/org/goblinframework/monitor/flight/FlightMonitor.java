package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.core.event.EventBus;
import org.goblinframework.core.monitor.FlightLocation;
import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
final public class FlightMonitor implements org.goblinframework.core.monitor.FlightMonitor, Ordered {

  public static final FlightMonitor INSTANCE = new FlightMonitor();

  private final ThreadLocal<FlightId> threadLocal = new ThreadLocal<>();

  private FlightMonitor() {
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  @NotNull
  @Override
  public FlightId createFlight(@NotNull FlightLocation location) {
    FlightId id = threadLocal.get();
    if (id != null) {
      // currently flight already exists.
      id.retain();
      return id;
    }
    // create a new flight
    id = new FlightId();
    id.retain();
    threadLocal.set(id);
    Flight flight = new Flight(id, location);
    FlightPool.INSTANCE.put(id, flight);
    return id;
  }

  @Nullable
  @Override
  public Flight terminateFlight() {
    FlightId id = threadLocal.get();
    if (id == null) {
      return null;
    }
    if (!id.release()) {
      return null;
    }
    threadLocal.remove();

    Flight flight = FlightPool.INSTANCE.remove(id);

    // Now, terminate the current flight
    FlightEvent event = new FlightEvent(flight);
    EventBus.publish(event);

    return flight;
  }

  @Override
  public void attachFlight(@NotNull Instruction instruction) {
    attachFlight(null, instruction);
  }

  @Override
  public void attachFlight(@Nullable org.goblinframework.core.monitor.FlightId flightId, @NotNull Instruction instruction) {
    org.goblinframework.core.monitor.FlightId id = flightId;
    if (id == null) {
      id = threadLocal.get();
    }
    if (id == null) {
      return;
    }
    Flight flight = FlightPool.INSTANCE.get(id);
    if (flight != null) {
      if (instruction instanceof org.goblinframework.core.monitor.Flight.Aware) {
        ((org.goblinframework.core.monitor.Flight.Aware) instruction).setFlight(flight);
      }
      flight.addInstruction(instruction);
    }
  }

  @Install
  final public static class Installer implements org.goblinframework.core.monitor.FlightMonitor {

    @NotNull
    @Override
    public FlightId createFlight(@NotNull FlightLocation location) {
      return INSTANCE.createFlight(location);
    }

    @Nullable
    @Override
    public Flight terminateFlight() {
      return INSTANCE.terminateFlight();
    }

    @Override
    public void attachFlight(@NotNull Instruction instruction) {
      INSTANCE.attachFlight(instruction);
    }

    @Override
    public void attachFlight(@Nullable org.goblinframework.core.monitor.FlightId flightId, @NotNull Instruction instruction) {
      INSTANCE.attachFlight(flightId, instruction);
    }
  }
}
