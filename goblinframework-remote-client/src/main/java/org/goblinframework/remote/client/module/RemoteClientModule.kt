package org.goblinframework.remote.client.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.service.ImportServiceProcessor
import org.goblinframework.remote.client.service.RemoteClientRegistry

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerConfigParser(RemoteClientConfigManager.INSTANCE.configParser)
    ctx.registerContainerBeanPostProcessor(ImportServiceProcessor.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteClientRegistry.INSTANCE.dispose()
    RemoteClientConfigManager.INSTANCE.dispose()
  }
}