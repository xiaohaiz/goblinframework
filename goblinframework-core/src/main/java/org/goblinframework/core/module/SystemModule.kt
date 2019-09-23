package org.goblinframework.core.module

import org.goblinframework.api.service.ServiceInstaller
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.module.spi.ManagementServerLifecycle

class SystemModule : GoblinModule {

  override fun name(): String {
    return "SYSTEM"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ServiceInstaller.firstOrNull(ManagementServerLifecycle::class.java)?.run { start() }
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ServiceInstaller.firstOrNull(ManagementServerLifecycle::class.java)?.run { stop() }
  }
}