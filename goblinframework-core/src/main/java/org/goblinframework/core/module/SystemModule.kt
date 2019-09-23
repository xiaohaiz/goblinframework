package org.goblinframework.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.service.ServiceInstaller
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext
import org.goblinframework.core.module.spi.ManagementServerLifecycle

@Install
class SystemModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.SYSTEM
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ServiceInstaller.firstOrNull(ManagementServerLifecycle::class.java)?.run { start() }
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ServiceInstaller.firstOrNull(ManagementServerLifecycle::class.java)?.run { stop() }
  }
}