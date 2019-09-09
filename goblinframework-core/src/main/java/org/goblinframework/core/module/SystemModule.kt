package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.module.spi.ManagementServer
import org.goblinframework.core.util.GoblinServiceLoader

class SystemModule : GoblinModule {

  override fun name(): String {
    return "SYSTEM"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    GoblinServiceLoader.installedFirst(ManagementServer::class.java)?.run { start() }
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    GoblinServiceLoader.installedFirst(ManagementServer::class.java)?.run { stop() }
  }
}