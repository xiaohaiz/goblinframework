package org.goblinframework.monitor.flight

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel

@GoblinEventChannel("/goblin/monitor")
class FlightFinishedEvent(val flight: Flight) : GoblinEvent() {

  init {
    isFair = true
  }

}
