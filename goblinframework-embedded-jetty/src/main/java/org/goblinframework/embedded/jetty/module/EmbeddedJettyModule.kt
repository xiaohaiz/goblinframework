package org.goblinframework.embedded.jetty.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule

@Install
class EmbeddedJettyModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.EMBEDDED_JETTY
  }
}