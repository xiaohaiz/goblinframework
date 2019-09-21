package org.goblinframework.transport.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext

class TransportModule : GoblinModule {

  override fun name(): String {
    return "TRANSPORT"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:SERVER")
        .next()
        .module("TRANSPORT:CLIENT")
        .finalize(ctx)
  }
}