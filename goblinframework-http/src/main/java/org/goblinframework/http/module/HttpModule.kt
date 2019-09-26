package org.goblinframework.http.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule

@Install
class HttpModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.HTTP
  }
}