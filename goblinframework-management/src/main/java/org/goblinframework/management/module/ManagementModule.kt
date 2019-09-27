package org.goblinframework.management.module

import org.goblinframework.api.core.Install
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
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