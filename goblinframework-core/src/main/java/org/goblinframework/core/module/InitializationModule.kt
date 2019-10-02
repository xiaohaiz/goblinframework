package org.goblinframework.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.reactor.CoreScheduler
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext

@Install
class InitializationModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.INITIALIZATION
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CoreScheduler.INSTANCE.dispose()
  }
}