package org.goblinframework.core.event.monitor

import java.lang.management.PlatformManagedObject

interface GoblinEventCountMXBean : PlatformManagedObject {

  fun getEvent(): String

  fun getAcceptedCount(): Long

  fun getRejectedCount(): Long

  fun getSucceedCount(): Long

  fun getFailedCount(): Long
}