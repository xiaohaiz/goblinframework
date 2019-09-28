package org.goblinframework.embedded.netty.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule

@Install
class EmbeddedNettyModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.EMBEDDED_NETTY
  }

}