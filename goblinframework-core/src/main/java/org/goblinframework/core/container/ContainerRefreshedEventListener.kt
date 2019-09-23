package org.goblinframework.core.container

import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener

@GoblinEventChannel("/goblin/core")
class ContainerRefreshedEventListener : GoblinEventListener {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
  }
}