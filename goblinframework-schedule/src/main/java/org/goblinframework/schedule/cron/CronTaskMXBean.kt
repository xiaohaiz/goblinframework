package org.goblinframework.schedule.cron

import java.lang.management.PlatformManagedObject

interface CronTaskMXBean : PlatformManagedObject {

  fun getName(): String

  fun getCronExpression(): String

  fun execute()
}