package org.goblinframework.core.event

import org.goblinframework.api.common.Ordered
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.util.ObjectUtils
import org.goblinframework.core.util.StopWatch
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean(type = "Core", name = "GoblinEventListener")
class GoblinEventListenerImpl
internal constructor(private val delegator: GoblinEventListener)
  : GoblinManagedObject(), GoblinEventListener, Ordered, GoblinEventListenerMXBean {

  private val watch = StopWatch()
  private val acceptedCount = LongAdder()
  private val rejectedCount = LongAdder()
  private val succeedCount = LongAdder()
  private val failedCount = LongAdder()

  override fun getOrder(): Int {
    return ObjectUtils.calculateOrder(delegator)
  }

  override fun accept(context: GoblinEventContext): Boolean {
    val ret = delegator.accept(context)
    if (ret) acceptedCount.increment() else rejectedCount.increment()
    return ret
  }

  override fun onEvent(context: GoblinEventContext) {
    try {
      delegator.onEvent(context)
      succeedCount.increment()
    } catch (ex: Exception) {
      failedCount.increment()
      throw ex
    }
  }

  override fun disposeBean() {
    watch.stop()
  }

  override fun getUpTime(): String {
    return watch.toString()
  }

  override fun getListener(): String {
    return delegator.toString()
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