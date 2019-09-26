package org.goblinframework.embedded.netty.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule

@Install
class EmbeddedNettyModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.EMBEDDED_NETTY
  }

}