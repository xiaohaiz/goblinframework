package org.goblinframework.monitor.flight;

import org.goblinframework.api.annotation.Singleton;

import java.util.concurrent.ConcurrentHashMap;

@Singleton
final class FlightPool extends ConcurrentHashMap<org.goblinframework.core.monitor.FlightId, Flight> {

  static final FlightPool INSTANCE = new FlightPool();

  private FlightPool() {
  }
}
