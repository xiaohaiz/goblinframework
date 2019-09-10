package org.goblinframework.core.container

import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@GoblinEventChannel("/goblin/core")
class ContainerRefreshedEventListener : GoblinEventListener {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
  }
}