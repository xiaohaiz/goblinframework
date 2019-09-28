package org.goblinframework.core.event

import java.util.concurrent.atomic.LongAdder

class EventBusWorkerEvent {

  var ctx: GoblinEventContextImpl? = null
  var listeners: List<GoblinEventListener>? = null
  var receivedCount: LongAdder? = null
  var succeedCount: LongAdder? = null
  var failedCount: LongAdder? = null

  fun clear() {
    ctx = null
    listeners = null
    receivedCount = null
    succeedCount = null
    failedCount = null
  }
}