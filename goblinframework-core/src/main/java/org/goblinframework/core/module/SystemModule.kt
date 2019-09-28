package org.goblinframework.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.core.ServiceInstaller
import org.goblinframework.core.management.IManagementServerManager
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext

@Install
class SystemModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.SYSTEM
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ServiceInstaller.firstOrNull(IManagementServerManager::class.java)?.start()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ServiceInstaller.firstOrNull(IManagementServerManager::class.java)?.stop()
  }
}