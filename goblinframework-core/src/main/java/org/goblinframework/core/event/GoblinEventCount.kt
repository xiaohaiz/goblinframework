package org.goblinframework.core.event

import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.event.GoblinEvent
import java.util.concurrent.atomic.LongAdder

class GoblinEventCount
internal constructor(private val type: Class<out GoblinEvent>)
  : GoblinManagedObject(), GoblinEventCountMXBean {

  internal val acceptedCount = LongAdder()
  internal val rejectedCount = LongAdder()
  internal val succeedCount = LongAdder()
  internal val failedCount = LongAdder()

  override fun getEvent(): String {
    return type.name
  }

  override fun getAcceptedCount(): Long {
    return acceptedCount.sum()
  }

  override fun getRejectedCount(): Long {
    return rejectedCount.sum()
  }

  override fun getSucceedCount(): Long {
    return succeedCount.sum()
  }

  override fun getFailedCount(): Long {
    return failedCount.sum()
  }
}