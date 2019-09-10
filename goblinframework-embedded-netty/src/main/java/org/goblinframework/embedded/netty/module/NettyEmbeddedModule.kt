package org.goblinframework.embedded.netty.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class NettyEmbeddedModule : GoblinChildModule {

  override fun name(): String {
    return "EMBEDDED:NETTY"
  }
}