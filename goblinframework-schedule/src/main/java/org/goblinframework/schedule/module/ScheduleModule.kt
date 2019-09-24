package org.goblinframework.schedule.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule

@Install
class ScheduleModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.SCHEDULE
  }
}