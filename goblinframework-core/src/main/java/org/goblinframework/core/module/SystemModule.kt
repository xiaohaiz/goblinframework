package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.module.spi.ManagementServerLifecycle
import org.goblinframework.core.util.ServiceInstaller

class SystemModule : GoblinModule {

  override fun name(): String {
    return "SYSTEM"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ServiceInstaller.installedFirst(ManagementServerLifecycle::class.java)?.run { start() }
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ServiceInstaller.installedFirst(ManagementServerLifecycle::class.java)?.run { stop() }
  }
}