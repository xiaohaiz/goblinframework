package org.goblinframework.core.event

class EventBusWorkerEvent {

  var ctx: GoblinEventContextImpl? = null
  var listeners: List<GoblinEventListener>? = null

  fun clear() {
    ctx = null
    listeners = null
  }
}