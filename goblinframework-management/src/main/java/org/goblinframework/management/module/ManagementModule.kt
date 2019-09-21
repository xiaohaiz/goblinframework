package org.goblinframework.management.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.management.controller.ManagementController
import org.goblinframework.management.server.ManagementServerManager

class ManagementModule : GoblinModule {

  override fun name(): String {
    return "MANAGEMENT"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.registerManagementController(ManagementController.INSTANCE)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ManagementServerManager.INSTANCE.dispose()
  }
}