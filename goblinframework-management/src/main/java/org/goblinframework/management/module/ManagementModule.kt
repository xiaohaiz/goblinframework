package org.goblinframework.management.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.management.controller.ManagementController
import org.goblinframework.management.server.ManagementServerManager

@Install
class ManagementModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.MANAGEMENT
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerManagementController(ManagementController.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ManagementServerManager.INSTANCE.dispose()
  }
}