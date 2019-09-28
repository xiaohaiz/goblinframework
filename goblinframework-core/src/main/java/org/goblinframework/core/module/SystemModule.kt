package org.goblinframework.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.management.IManagementServerManager
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext

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