package org.goblinframework.schedule.cron

import java.lang.management.PlatformManagedObject

interface CronTaskMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getName(): String

  fun getCronExpression(): String

  fun getConcurrent(): Boolean

  fun getExecuteTimes(): Long

  fun execute()
}