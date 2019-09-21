package org.goblinframework.monitor.module.test

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.common.Ordered
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.monitor.FlightEvent

@Install
@GoblinEventChannel("/goblin/monitor")
class UnitTestFlightListener : GoblinEventListener, Ordered {

  override fun getOrder(): Int {
    return Ordered.LOWEST_PRECEDENCE
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is FlightEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as FlightEvent
    UnitTestFlightRecorder.onFlightFinished(event.flight.flightId())
  }
}
