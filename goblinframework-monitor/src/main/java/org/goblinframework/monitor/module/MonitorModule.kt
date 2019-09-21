package org.goblinframework.monitor.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.monitor.message.TimedTouchableMessageBufferManager

@Install
class MonitorModule : GoblinModule {

  override fun name(): String {
    return "MONITOR"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    TimedTouchableMessageBufferManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    TimedTouchableMessageBufferManager.INSTANCE.dispose()
  }
}