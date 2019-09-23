package org.goblinframework.core.event.worker

import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.core.event.context.GoblinEventContextImpl

class EventBusWorkerEvent {

  var ctx: GoblinEventContextImpl? = null
  var listeners: List<GoblinEventListener>? = null

  fun clear() {
    ctx = null
    listeners = null
  }
}