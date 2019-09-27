package org.goblinframework.monitor.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.event.EventBus
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.monitor.message.TimedTouchableMessageBufferManager
import org.goblinframework.monitor.module.monitor.FlightPrettyPrinterListener
import org.goblinframework.monitor.module.test.UnitTestFlightListener
import org.goblinframework.monitor.module.test.UnitTestFlightRecorder
import org.goblinframework.monitor.point.MonitorPointManager

@Install
class MonitorModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.MONITOR
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(UnitTestFlightRecorder.INSTANCE)
    EventBus.subscribe(FlightPrettyPrinterListener.INSTANCE)
    EventBus.subscribe(UnitTestFlightListener.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MonitorPointManager.INSTANCE.dispose()
    TimedTouchableMessageBufferManager.INSTANCE.dispose()
    EventBus.unsubscribe(FlightPrettyPrinterListener.INSTANCE)
    EventBus.unsubscribe(UnitTestFlightListener.INSTANCE)
  }
}