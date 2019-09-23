package org.goblinframework.bootstrap.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule

@Install
class BootstrapModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.BOOTSTRAP
  }
}