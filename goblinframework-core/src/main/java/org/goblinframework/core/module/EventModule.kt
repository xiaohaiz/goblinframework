package org.goblinframework.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.core.event.boss.EventBusBoss

@Install
class EventModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EVENT
  }

  override fun install(ctx: ModuleInstallContext) {
    EventBusBoss.INSTANCE.install()
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    EventBusBoss.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    EventBusBoss.INSTANCE.dispose()
  }
}