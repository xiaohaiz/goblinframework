package org.goblinframework.schedule.cron

import org.goblinframework.api.schedule.CronTask
import org.goblinframework.api.schedule.ICronTaskManager
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "schedule")
class CronTaskManager private constructor()
  : GoblinManagedObject(), ICronTaskManager, CronTaskManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CronTaskManager()
  }

  override fun register(task: CronTask) {
  }

  override fun unregister(task: CronTask) {
  }
}