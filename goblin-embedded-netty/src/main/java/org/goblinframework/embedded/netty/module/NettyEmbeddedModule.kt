package org.goblinframework.embedded.netty.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.embedded.netty.server.NettyEmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerFactoryManager

@Install
class NettyEmbeddedModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.EMBEDDED_NETTY
  }

  override fun install(ctx: ModuleInstallContext) {
    EmbeddedServerFactoryManager.INSTANCE.register(NettyEmbeddedServerFactory.INSTANCE)
  }
}