package org.goblinframework.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.core.event.boss.EventBusBoss
import org.goblinframework.core.event.timer.TimerEventGenerator

@Install
class EventModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EVENT
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerEventChannel("/goblin/core", 32768, 0)
    ctx.registerEventChannel("/goblin/timer", 32768, 4)
    ctx.registerEventChannel("/goblin/monitor", 65536, 8)
    TimerEventGenerator.INSTANCE.install()
    EventBusBoss.INSTANCE.install()
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    EventBusBoss.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    TimerEventGenerator.INSTANCE.dispose()
    EventBusBoss.INSTANCE.dispose()
  }
}