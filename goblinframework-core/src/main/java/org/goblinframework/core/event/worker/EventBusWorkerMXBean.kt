package org.goblinframework.core.event.worker

import java.lang.management.PlatformManagedObject

interface EventBusWorkerMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getChannel(): String

  fun getBufferSize(): Int

  fun getRemainingCapacity(): Int

  fun getWorkers(): Int

  fun getPublishedCount(): Long

  fun getDiscardedCount(): Long
}