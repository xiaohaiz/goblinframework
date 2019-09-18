package org.goblinframework.remote.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.remote.server.handler.RemoteServer
import org.goblinframework.remote.server.service.RemoteServiceManager

@Install
class RemoteServerModule : GoblinChildModule {

  override fun name(): String {
    return "REMOTE:SERVER"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    RemoteServer.INSTANCE.start()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    RemoteServer.INSTANCE.dispose()
    RemoteServiceManager.INSTANCE.dispose()
  }
}