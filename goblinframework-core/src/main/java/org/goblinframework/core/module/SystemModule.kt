package org.goblinframework.core.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.management.IManagementServerManager
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext

@Install
class SystemModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.SYSTEM
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    IManagementServerManager.instance()?.start()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    IManagementServerManager.instance()?.stop()
  }
}