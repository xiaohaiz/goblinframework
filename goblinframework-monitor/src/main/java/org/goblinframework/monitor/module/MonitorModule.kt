package org.goblinframework.monitor.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.monitor.message.TimedTouchableMessageBufferManager
import org.goblinframework.monitor.module.test.UnitTestFlightRecorder
import org.goblinframework.monitor.point.MonitorPointManager

@Install
class MonitorModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.MONITOR
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(UnitTestFlightRecorder.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MonitorPointManager.INSTANCE.dispose()
    TimedTouchableMessageBufferManager.INSTANCE.dispose()
  }
}