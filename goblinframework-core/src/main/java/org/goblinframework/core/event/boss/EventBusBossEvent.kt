package org.goblinframework.core.event.boss

import org.goblinframework.core.event.GoblinEventContextImpl
import java.util.concurrent.atomic.LongAdder

class EventBusBossEvent {

  var ctx: GoblinEventContextImpl? = null
  var receivedCount: LongAdder? = null
  var workerMissedCount: LongAdder? = null
  var listenerMissedCount: LongAdder? = null
  var dispatchedCount: LongAdder? = null

  internal fun clear() {
    ctx = null
    receivedCount = null
    workerMissedCount = null
    listenerMissedCount = null
    dispatchedCount = null
  }

}