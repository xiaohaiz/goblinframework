package org.goblinframework.schedule.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.schedule.cron.CronTaskManager

@Install
class ScheduleModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.SCHEDULE
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CronTaskManager.INSTANCE.dispose()
  }
}