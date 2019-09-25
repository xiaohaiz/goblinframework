package org.goblinframework.core.event.worker

import java.lang.management.PlatformManagedObject

interface EventBusWorkerMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getChannel(): String
}