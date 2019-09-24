package org.goblinframework.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.core.event.boss.EventBusBoss
import org.goblinframework.core.system.SubModuleEventListener

@Install
class EventModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EVENT
  }

  override fun install(ctx: ModuleInstallContext) {
    EventBusBoss.INSTANCE.initialize()
    EventBus.subscribe(SubModuleEventListener.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    EventBus.unsubscribe(SubModuleEventListener.INSTANCE)
    EventBusBoss.INSTANCE.dispose()
  }
}