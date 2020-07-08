package org.goblinframework.queue.consumer.executor

import java.lang.management.PlatformManagedObject

interface ListenerExecutorsMXBean : PlatformManagedObject {
  fun getPoolSize(): Int

  fun getActiveCount(): Int

  fun getCorePoolSize(): Int

  fun getMaximumPoolSize(): Int

  fun getLargestPoolSize(): Int

  fun getTaskCount(): Long

  fun getCompletedTaskCount(): Long

  fun getShutdown(): Boolean

  fun getTerminated(): Boolean

  fun getTerminating(): Boolean
}