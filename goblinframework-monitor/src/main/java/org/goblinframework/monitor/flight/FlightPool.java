package org.goblinframework.monitor.flight;

import org.goblinframework.api.core.Singleton;
import org.goblinframework.api.monitor.FlightId;

import java.util.concurrent.ConcurrentHashMap;

@Singleton
final class FlightPool extends ConcurrentHashMap<FlightId, FlightImpl> {

  static final FlightPool INSTANCE = new FlightPool();

  private FlightPool() {
  }
}
