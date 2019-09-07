package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel

@GoblinEventChannel("/goblin/core")
class GoblinChildModuleEvent(val ctx: Any, val childModules: List<GoblinChildModule>) : GoblinEvent() {

  init {
    when (ctx) {
      is GoblinModuleShutdownContext -> isRaiseException = false
      is GoblinModuleFinalizeContext -> isRaiseException = false
    }
  }
}