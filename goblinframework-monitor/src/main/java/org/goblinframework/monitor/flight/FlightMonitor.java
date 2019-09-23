package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.api.event.EventBus;
import org.goblinframework.api.monitor.FlightLocation;
import org.goblinframework.api.monitor.IFlightMonitor;
import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.core.monitor.FlightEvent;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.monitor.module.monitor.DOT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
final public class FlightMonitor implements IFlightMonitor, Ordered {

  public static final FlightMonitor INSTANCE = new FlightMonitor();

  private final ThreadLocal<FlightIdImpl> threadLocal = new ThreadLocal<>();

  private FlightMonitor() {
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  @NotNull
  @Override
  public FlightIdImpl createFlight(@NotNull FlightLocation location) {
    FlightIdImpl id = threadLocal.get();
    if (id != null) {
      // currently flight already exists.
      id.retain();
      return id;
    }
    // create a new flight
    id = new FlightIdImpl();
    id.retain();
    threadLocal.set(id);
    FlightImpl flight = new FlightImpl(id, location);
    FlightPool.INSTANCE.put(id, flight);
    return id;
  }

  @Nullable
  @Override
  public FlightImpl terminateFlight() {
    FlightIdImpl id = threadLocal.get();
    if (id == null) {
      return null;
    }
    if (!id.release()) {
      return null;
    }
    threadLocal.remove();

    FlightImpl flight = FlightPool.INSTANCE.remove(id);
    flight.stop();

    // Now, terminate the current flight
    FlightEvent event = new FlightEvent(flight);
    event.setFair(true);
    EventBus.publish(event);

    return flight;
  }

  @Override
  public void attachFlight(@NotNull Instruction instruction) {
    attachFlight(null, instruction);
  }

  @Override
  public void attachFlight(@Nullable org.goblinframework.api.monitor.FlightId flightId, @NotNull Instruction instruction) {
    org.goblinframework.api.monitor.FlightId id = flightId;
    if (id == null) {
      id = threadLocal.get();
    }
    if (id == null) {
      return;
    }
    FlightImpl flight = FlightPool.INSTANCE.get(id);
    if (flight != null) {
      if (instruction instanceof org.goblinframework.api.monitor.Flight.Aware) {
        ((org.goblinframework.api.monitor.Flight.Aware) instruction).setFlight(flight);
      }
      flight.addInstruction(instruction);
    }
  }

  @Override
  public void dot(@Nullable String name) {
    String dotName = StringUtils.defaultString(name, "unspecified");
    attachFlight(new DOT(dotName));
  }

  @Install
  final public static class Installer implements IFlightMonitor {

    @NotNull
    @Override
    public FlightIdImpl createFlight(@NotNull FlightLocation location) {
      return INSTANCE.createFlight(location);
    }

    @Nullable
    @Override
    public FlightImpl terminateFlight() {
      return INSTANCE.terminateFlight();
    }

    @Override
    public void attachFlight(@NotNull Instruction instruction) {
      INSTANCE.attachFlight(instruction);
    }

    @Override
    public void attachFlight(@Nullable org.goblinframework.api.monitor.FlightId flightId, @NotNull Instruction instruction) {
      INSTANCE.attachFlight(flightId, instruction);
    }

    @Override
    public void dot(@Nullable String name) {
      INSTANCE.dot(name);
    }
  }
}
