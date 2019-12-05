package org.goblinframework.remote.client.dispatcher.request

import java.lang.management.PlatformManagedObject

interface RemoteClientRequestThreadPoolMXBean : PlatformManagedObject {

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