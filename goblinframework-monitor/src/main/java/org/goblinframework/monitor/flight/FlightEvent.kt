package org.goblinframework.monitor.flight

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel

@GoblinEventChannel("/goblin/monitor")
class FlightEvent(val flight: Flight) : GoblinEvent() {

  init {
    isFair = true
  }

}
