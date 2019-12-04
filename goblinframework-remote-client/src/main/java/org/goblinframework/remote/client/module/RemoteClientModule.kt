package org.goblinframework.remote.client.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.remote.client.dispatcher.request.RemoteClientRequestDispatcher
import org.goblinframework.remote.client.dispatcher.response.RemoteClientResponseDispatcher
import org.goblinframework.remote.client.invocation.RemoteClientFilterManager
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.module.container.InjectRemoteService
import org.goblinframework.remote.client.module.runtime.RemoteClientTranscoderManager
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformationManager
import org.goblinframework.remote.client.service.RemoteServiceClientManager
import org.goblinframework.remote.client.service.RemoteServiceClientWarmUpManager
import org.goblinframework.remote.client.transport.RemoteTransportClientManager

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerContainerBeanPostProcessor(InjectRemoteService.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteClientConfigManager.INSTANCE.initialize()
    RemoteServiceInformationManager.INSTANCE.initialize()
    RemoteClientTranscoderManager.INSTANCE.initialize()
    RemoteClientResponseDispatcher.INSTANCE.initialize()
    RemoteClientFilterManager.INSTANCE.initialize()
    RemoteClientRequestDispatcher.INSTANCE.initialize()
    RemoteTransportClientManager.INSTANCE.initialize()
    RemoteServiceClientWarmUpManager.INSTANCE.initialize()
    RemoteServiceClientManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServiceClientManager.INSTANCE.dispose()
    RemoteServiceClientWarmUpManager.INSTANCE.dispose()
    RemoteTransportClientManager.INSTANCE.dispose()
    RemoteClientRequestDispatcher.INSTANCE.dispose()
    RemoteClientFilterManager.INSTANCE.dispose()
    RemoteClientResponseDispatcher.INSTANCE.dispose()
    RemoteClientTranscoderManager.INSTANCE.dispose()
    RemoteServiceInformationManager.INSTANCE.dispose()
    RemoteClientConfigManager.INSTANCE.dispose()
  }
}