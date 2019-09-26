package org.goblinframework.remote.client.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.remote.client.service.ImportServiceProcessor

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerContainerBeanPostProcessor(ImportServiceProcessor.INSTANCE)
  }
}