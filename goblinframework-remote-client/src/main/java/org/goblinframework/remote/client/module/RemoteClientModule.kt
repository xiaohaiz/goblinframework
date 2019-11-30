package org.goblinframework.remote.client.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteClientConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteClientConfigManager.INSTANCE.dispose()
  }
}