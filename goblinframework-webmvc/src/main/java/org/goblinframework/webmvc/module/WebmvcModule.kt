package org.goblinframework.webmvc.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.goblinframework.webmvc.module.management.WebmvcManagementController

class WebmvcModule : GoblinModule {

  override fun name(): String {
    return "WEBMVC"
  }

  override fun managementEntrance(): String? {
    return "/webmvc/index.do"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.registerManagementController(WebmvcManagementController.INSTANCE)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    RequestHandlerManagerBuilder.INSTANCE.dispose()
  }
}