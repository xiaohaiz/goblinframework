package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.function.Ordered;
import org.goblinframework.core.event.EventBus;
import org.goblinframework.core.monitor.*;
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

  @Nullable
  @Override
  public Flight currentFlight() {
    FlightIdImpl id = threadLocal.get();
    if (id == null) {
      return null;
    }
    return FlightPool.INSTANCE.get(id);
  }

  @Override
  public void attachFlight(@Nullable FlightId flightId, @NotNull Instruction instruction) {
    FlightImpl flight;
    if (flightId == null) {
      flight = (FlightImpl) currentFlight();
    } else {
      flight = FlightPool.INSTANCE.get(flightId);
    }
    if (flight != null) {
      if (instruction instanceof Flight.Aware) {
        ((Flight.Aware) instruction).setFlight(flight);
      }
      flight.addInstruction(instruction);
    }
  }

  @Override
  public void dot(@Nullable String name) {
    String dotName = StringUtils.defaultString(name, "unspecified");
    FlightId flightId = FlightRecorder.currentThreadFlightId();
    attachFlight(flightId, new DOT(dotName));
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

    @Nullable
    @Override
    public Flight currentFlight() {
      return INSTANCE.currentFlight();
    }

    @Override
    public void attachFlight(@Nullable FlightId flightId, @NotNull Instruction instruction) {
      INSTANCE.attachFlight(flightId, instruction);
    }

    @Override
    public void dot(@Nullable String name) {
      INSTANCE.dot(name);
    }
  }
}
