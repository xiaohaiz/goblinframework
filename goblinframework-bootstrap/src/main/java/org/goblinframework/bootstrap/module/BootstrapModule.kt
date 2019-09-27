package org.goblinframework.bootstrap.module

import org.goblinframework.api.core.Install
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule

@Install
class BootstrapModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.BOOTSTRAP
  }
}