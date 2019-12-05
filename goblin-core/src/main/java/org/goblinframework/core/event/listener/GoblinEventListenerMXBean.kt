package org.goblinframework.core.event.listener

import java.lang.management.PlatformManagedObject

interface GoblinEventListenerMXBean : PlatformManagedObject {

  fun getId(): String

  fun getUpTime(): String

  fun getListener(): String

  fun getAcceptedCount(): Long

  fun getRejectedCount(): Long

  fun getSucceedCount(): Long

  fun getFailedCount(): Long

  fun getEventCountList(): Array<GoblinEventCountMXBean>
}