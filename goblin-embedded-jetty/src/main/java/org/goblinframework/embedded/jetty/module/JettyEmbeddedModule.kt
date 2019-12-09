package org.goblinframework.embedded.jetty.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.embedded.jetty.server.JettyEmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerFactoryManager

@Install
class JettyEmbeddedModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.EMBEDDED_JETTY
  }

  override fun install(ctx: ModuleInstallContext) {
    EmbeddedServerFactoryManager.INSTANCE.register(JettyEmbeddedServerFactory.INSTANCE)
  }
}