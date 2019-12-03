package org.goblinframework.core.event.boss

import org.goblinframework.core.event.timer.TimerEventGeneratorMXBean
import org.goblinframework.core.event.worker.EventBusWorkerMXBean
import java.lang.management.PlatformManagedObject

interface EventBusBossMXBean : PlatformManagedObject {

  fun getUpTime(): String?

  fun getBufferSize(): Int

  fun getRemainingCapacity(): Int

  fun getWorkers(): Int

  fun getPublishedCount(): Long

  fun getDiscardedCount(): Long

  fun getReceivedCount(): Long

  fun getWorkerMissedCount(): Long

  fun getListenerMissedCount(): Long

  fun getDispatchedCount(): Long

  fun getEventBusWorkerList(): Array<EventBusWorkerMXBean>

  fun getTimerEventGenerator(): TimerEventGeneratorMXBean?

}