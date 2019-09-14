package org.goblinframework.remote.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule

@Install
class RemoteServerModule : GoblinChildModule {

  override fun name(): String {
    return "REMOTE:SERVER"
  }
}