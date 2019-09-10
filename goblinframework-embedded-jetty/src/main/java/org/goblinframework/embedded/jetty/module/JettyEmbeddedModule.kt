package org.goblinframework.embedded.jetty.module

import org.goblinframework.core.bootstrap.GoblinModule

class JettyEmbeddedModule : GoblinModule {

  override fun name(): String {
    return "EMBEDDED:JETTY"
  }
}