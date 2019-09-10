package org.goblinframework.webmvc.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder

class WebmvcModule : GoblinModule {

  override fun name(): String {
    return "WEBMVC"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    RequestHandlerManagerBuilder.INSTANCE.close()
  }
}