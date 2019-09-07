package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@GoblinEventChannel("/goblin/bootstrap")
class GoblinChildModuleEventListener private constructor() : GoblinEventListener {

  companion object {
    @JvmField val INSTANCE = GoblinChildModuleEventListener()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is GoblinChildModuleEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as GoblinChildModuleEvent
    when (event.ctx) {
      is GoblinModuleInitializeContext -> {
        event.childModules.forEach { it.initialize(event.ctx) }
      }
      is GoblinModuleBootstrapContext -> {
        event.childModules.forEach { it.bootstrap(event.ctx) }
      }
      is GoblinModuleShutdownContext -> {
        event.childModules.reversed().forEach { it.shutdown(event.ctx) }
      }
      is GoblinModuleFinalizeContext -> {
        event.childModules.reversed().forEach { it.finalize(event.ctx) }
      }
    }
  }
}