package org.goblinframework.webmvc.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.goblinframework.webmvc.module.management.WebmvcManagementController

@Install
class WebmvcModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.WEBMVC
  }

  override fun managementEntrance(): String? {
    return "/webmvc/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerManagementController(WebmvcManagementController.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RequestHandlerManagerBuilder.INSTANCE.dispose()
  }
}