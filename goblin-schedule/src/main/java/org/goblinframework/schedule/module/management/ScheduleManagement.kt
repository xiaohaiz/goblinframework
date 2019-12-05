package org.goblinframework.schedule.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.schedule.cron.CronTaskManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/schedule")
class ScheduleManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = ScheduleManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("cronTaskManagerMXBean", CronTaskManager.INSTANCE)
    return "schedule/index"
  }
}