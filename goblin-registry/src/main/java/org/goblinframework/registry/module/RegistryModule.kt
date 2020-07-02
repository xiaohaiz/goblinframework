package org.goblinframework.registry.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule

@Install
class RegistryModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.REGISTRY
  }

}